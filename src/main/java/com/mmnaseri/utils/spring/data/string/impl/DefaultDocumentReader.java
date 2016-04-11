package com.mmnaseri.utils.spring.data.string.impl;

import com.mmnaseri.utils.spring.data.error.ParserException;
import com.mmnaseri.utils.spring.data.string.DocumentReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public class DefaultDocumentReader implements DocumentReader {

    private static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private final String document;
    private int column;
    private int line;
    private int cursor;

    /**
     * Instantiates the reader while taking in the document to be read through
     * @param document    the document
     */
    public DefaultDocumentReader(String document) {
        this.document = document;
        reset();
    }

    /**
     * This will return the parts of document not yet read through by the reader
     * @return the part of the document not processed by the reader
     */
    @Override
    public String rest() {
        return document.substring(cursor);
    }

    /**
     * Processes the character input, deciding on how to change position information
     * @param c    the character recently read
     * @return the input character
     */
    private char processChar(char c) {
        if (c == '\n') {
            nextLine();
        } else {
            column ++;
        }
        return c;
    }

    /**
     * Processes the text input, deciding on how to change position information
     * @param text    the text recently read
     * @return the input text
     */
    private String  processText(String text) {
        for (int i = 0; i < text.length(); i++) {
            processChar(text.charAt(i));
        }
        return text;
    }

    /**
     * Will change the caret position to the next line
     */
    private void nextLine() {
        line ++;
        column = 1;
    }

    /**
     * This method will skip all the characters matching the pattern from this point in the document
     * onwards (if any)
     */
    @Override
    public void skip(Pattern pattern) {
        final Matcher matcher = pattern.matcher(rest());
        if (!matcher.find() || matcher.start() != 0) {
            return;
        }
        cursor += matcher.group().length();
        processText(matcher.group());
    }

    /**
     * This method will determine whether the indicated pattern can be found at this point in the document or not
     * @param pattern    the lookup pattern
     * @return <code>true</code> if the pattern can be found
     */
    @Override
    public boolean has(Pattern pattern) {
        final Matcher matcher = pattern.matcher(rest());
        return matcher.find() && matcher.start() == 0;
    }

    /**
     * This method will determine whether the indicated pattern can be found at this point in the document or not
     * @param pattern    the lookup pattern
     * @return <code>true</code> if the pattern can be found
     */
    @Override
    public boolean has(String pattern) {
        return has(Pattern.compile(pattern.startsWith("^") ? pattern : "^" + pattern, Pattern.DOTALL | Pattern.MULTILINE));
    }

    /**
     * Determines whether we have hit the end of the document or not
     * @return <code>true</code> if we have no more to go
     */
    @Override
    public boolean hasMore() {
        return !rest().isEmpty();
    }

    /**
     * Will attempt to read the string matching the given parameter. If the string matched with this pattern
     * does not start at the current point in the document, the result will be considered to be negative.
     * @param pattern            the compiled pattern to be matched against
     * @param skipWhitespaces    will cause a call to {@link #skip(java.util.regex.Pattern)} with whitespace pattern
     *                           before going forth
     * @return the string read by the method, or <code>null</code> if it cannot be found
     * @see Pattern#compile(String)
     * @see Pattern#compile(String, int)
     */
    @Override
    public String read(Pattern pattern, boolean skipWhitespaces) {
        if (skipWhitespaces) {
            skip(WHITESPACE);
        }
        final Matcher matcher = pattern.matcher(rest());
        if (matcher.find() && matcher.start() == 0) {
            cursor += matcher.group().length();
            return processText(matcher.group());
        }
        return null;
    }

    /**
     * Will attempt to read the string matching the given parameter. If the string matched with this pattern
     * does not start at the current point in the document, the result will be considered to be negative.
     * @param pattern            the compiled pattern to be matched against
     * @param skipWhitespaces    will cause a call to {@link #skip(java.util.regex.Pattern)} with whitespace pattern
     *                           before going forth
     * @return the string read by the method, or <code>null</code> if it cannot be found
     * @see Pattern#compile(String)
     * @see Pattern#compile(String, int)
     */
    @Override
    public String read(String pattern, boolean skipWhitespaces) {
        return read(Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE), skipWhitespaces);
    }

    /**
     * Will attempt to read the string matching the given parameter. If the string matched with this pattern
     * does not start at the current point in the document, the result will be considered to be negative.
     * @param pattern            the compiled pattern to be matched against
     * @return the string read by the method, or <code>null</code> if it cannot be found
     * @see Pattern#compile(String)
     * @see Pattern#compile(String, int)
     */
    @Override
    public String read(Pattern pattern) {
        return read(pattern, false);
    }

    /**
     * Will attempt to read the string matching the given parameter. If the string matched with this pattern
     * does not start at the current point in the document, the result will be considered to be negative.
     * @param pattern            the compiled pattern to be matched against
     * @return the string read by the method, or <code>null</code> if it cannot be found
     * @see Pattern#compile(String)
     * @see Pattern#compile(String, int)
     */
    @Override
    public String read(String pattern) {
        return read(pattern, false);
    }

    /**
     * This will attempt to read string matching the given pattern from the document at the current point
     * indicated by the cursor. If failed to do so, the method will be expected to throw an exception or take corrective measures.
     * @param pattern            the regular to query for
     * @param skipWhitespaces    will cause a call to {@link #skip(java.util.regex.Pattern)} with whitespace pattern
     *                           before going forth
     * @return the read string
     */
    @Override
    public String expect(Pattern pattern, boolean skipWhitespaces) {
        final String token = read(pattern, skipWhitespaces);
        if (token == null) {
            throw new ParserException("Expected pattern '" + pattern.pattern() + "' was not encountered in document: " + document);
        }
        return token;
    }

    /**
     * This will attempt to read string matching the given pattern from the document at the current point
     * indicated by the cursor. If failed to do so, the method will be expected to throw an exception or take corrective measures.
     * @param pattern            the regular to query for
     * @return the read string
     */
    @Override
    public String expect(String pattern) {
        return expect(pattern, false);
    }

    /**
     * This will attempt to read string matching the given pattern from the document at the current point
     * indicated by the cursor. If failed to do so, the method will be expected to throw an exception or take corrective measures.
     * @param pattern            the regular to query for
     * @param skipWhitespaces    will cause a call to {@link #skip(java.util.regex.Pattern)} with whitespace pattern
     *                           before going forth
     * @return the read string
     */
    @Override
    public String expect(String pattern, boolean skipWhitespaces) {
        return expect(Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE), skipWhitespaces);
    }

    /**
     * This will cause the state of the reading to be reset. The cursor will be set back to the beginning of the document,
     * and the line/column positioning data will be reset to their initial value.
     */
    @Override
    public void reset() {
        cursor = 0;
        line = 1;
        column = 1;
    }

    /**
     * @return the current line of the document at which the reader stands. Starts at 1.
     */
    @Override
    public int getLine() {
        return line;
    }

    /**
     * @return the column in the current line to which the reader has advanced. Starts at 1.
     */
    @Override
    public int getColumn() {
        return column;
    }

    /**
     * Moves back the specified number of characters
     * @param count character count to backtrack by
     */
    @Override
    public void backtrack(int count) {
        if (cursor - count < 0) {
            throw new ParserException("Cannot backtrack more than the read portion of the document.");
        }
        cursor -= count;
    }

}
