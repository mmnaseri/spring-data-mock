package com.mmnaseri.utils.spring.data.string.impl;

import com.mmnaseri.utils.spring.data.error.ParserException;
import com.mmnaseri.utils.spring.data.string.DocumentReader;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
@SuppressWarnings("unused")
public class DefaultDocumentReader implements DocumentReader {

    private final String document;
    private int cursor;

    /**
     * Instantiates the reader while taking in the document to be read through
     *
     * @param document the document
     */
    public DefaultDocumentReader(String document) {
        this.document = document;
        reset();
    }

    /**
     * This will return the parts of document not yet read through by the reader
     *
     * @return the part of the document not processed by the reader
     */
    @Override
    public String rest() {
        return document.substring(cursor);
    }

    /**
     * This method will determine whether the indicated pattern can be found at this point in the document or not
     *
     * @param pattern the lookup pattern
     * @return <code>true</code> if the pattern can be found
     */
    @Override
    public boolean has(Pattern pattern) {
        final Matcher matcher = pattern.matcher(rest());
        return matcher.find() && matcher.start() == 0;
    }

    /**
     * This method will determine whether the indicated pattern can be found at this point in the document or not
     *
     * @param pattern the lookup pattern
     * @return <code>true</code> if the pattern can be found
     */
    @Override
    public boolean has(String pattern) {
        if (!pattern.startsWith("^")) {
            pattern = "^" + pattern;
        }
        return has(Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE));
    }

    /**
     * Determines whether we have hit the end of the document or not
     *
     * @return <code>true</code> if we have no more to go
     */
    @Override
    public boolean hasMore() {
        return !rest().isEmpty();
    }

    /**
     * Will attempt to read the string matching the given parameter. If the string matched with this pattern does not
     * start at the current point in the document, the result will be considered to be negative.
     *
     * @param pattern the compiled pattern to be matched against
     * @return the string read by the method, or <code>null</code> if it cannot be found
     * @see Pattern#compile(String)
     * @see Pattern#compile(String, int)
     */
    @Override
    public String read(Pattern pattern) {
        final Matcher matcher = pattern.matcher(rest());
        if (matcher.find() && matcher.start() == 0) {
            cursor += matcher.group().length();
            return matcher.group();
        }
        return null;
    }

    /**
     * Will attempt to read the string matching the given parameter. If the string matched with this pattern does not
     * start at the current point in the document, the result will be considered to be negative.
     *
     * @param pattern the compiled pattern to be matched against
     * @return the string read by the method, or <code>null</code> if it cannot be found
     * @see Pattern#compile(String)
     * @see Pattern#compile(String, int)
     */
    @Override
    public String read(String pattern) {
        return read(Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE));
    }

    /**
     * This will attempt to read string matching the given pattern from the document at the current point indicated by
     * the cursor. If failed to do so, the method will be expected to throw an exception or take corrective measures.
     *
     * @param pattern the regular to query for
     * @return the read string
     */
    @Override
    public String expect(Pattern pattern) {
        final String token = read(pattern);
        if (token == null) {
            throw new ParserException(
                    "Expected pattern '" + pattern.pattern() + "' was not encountered in document: " + document);
        }
        return token;
    }

    @Override
    public String expect(String pattern) {
        return expect(Pattern.compile(pattern, Pattern.DOTALL | Pattern.MULTILINE));
    }

    /**
     * This will cause the state of the reading to be reset. The cursor will be set back to the beginning of the
     * document, and the line/column positioning data will be reset to their initial value.
     */
    @Override
    public void reset() {
        cursor = 0;
    }

    /**
     * Moves back the specified number of characters
     *
     * @param count character count to backtrack by
     */
    @Override
    public void backtrack(int count) {
        cursor -= count;
    }

}
