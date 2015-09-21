package com.mmnaseri.utils.spring.data.string.impl;

import com.mmnaseri.utils.spring.data.string.DocumentReader;
import com.mmnaseri.utils.spring.data.string.SnippetParser;
import com.mmnaseri.utils.spring.data.string.Token;
import com.mmnaseri.utils.spring.data.string.TokenReader;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public class DefaultDocumentReader implements DocumentReader {

    public static final Pattern WHITESPACE = Pattern.compile("\\s+");
    private final String document;
    private final Set<TokenReader> readers;
    private final List<ReaderSnapshot> snapshots;
    private int column;
    private int line;
    private int cursor;
    private long timestamp;

    /**
     * Instantiates the reader while taking in the document to be read through
     * @param document    the document
     */
    public DefaultDocumentReader(String document) {
        this.document = document;
        readers = new HashSet<TokenReader>();
        snapshots = new ArrayList<ReaderSnapshot>();
        reset();
    }

    /**
     * This method will add a token reader to the document reader so that tokens can be read more easily
     * @param reader    the reader to be registered with the document reader
     */
    @Override
    public void addReader(TokenReader reader) {
        this.readers.add(reader);
    }

    /**
     * This should give report as to where the reader stands within the document at this moment
     * @return the part of document currently read through
     */
    @Override
    public String taken() {
        return document.substring(0, cursor);
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
     * This method will determine if another token can be found in the document
     * @return <code>true</code> if a valid token exists to be read
     */
    @Override
    public boolean hasTokens() {
        for (TokenReader reader : readers) {
            if (reader.read(rest()) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * This method will determine if another token can be found in the document
     * @param tokenReader    the token designator for discovering tokens
     * @return <code>true</code> if a token that can be read by the given reader exists in the document
     */
    @Override
    public boolean hasTokens(TokenReader tokenReader) {
        return tokenReader.read(rest()) != null;
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
     * Will return the next character
     * @return next character in the document stream
     */
    @Override
    public char nextChar() {
        if (!hasMore()) {
            throw new IllegalStateException();
        }
        return processChar(document.charAt(cursor++));
    }

    /**
     * Will determine possible tokens that can be read from the document
     * @return a set of all tokens that could be read from the document
     */
    @Override
    public Set<Token> nextToken() {
        final HashSet<Token> tokens = new HashSet<Token>();
        for (TokenReader reader : readers) {
            final Token token = reader.read(rest());
            if (token != null) {
                tokens.add(token);
            }
        }
        return tokens;
    }

    /**
     * Will give the next token. If no tokens can be found, the method will either take further action
     * to fix this, or throw an Exception. You can confer {@link #hasTokens} to see if another token exists
     * in the document.<br/>
     * <strong>NB</strong> This method will attempt to read the <em>first</em> identifiable token, meaning
     * that if one token is the prefix of another, then the first will be discovered.
     *
     * @param tokenReader       the token designator
     * @return next token (if any can be found)
     */
    @Override
    public Token nextToken(TokenReader tokenReader) {
        return tokenReader.read(rest());
    }

    /**
     * Works the same as {@link #nextToken(TokenReader)} with the exception that if the token cannot be read,
     * an exception will be thrown
     *
     * @param tokenReader       the token designator
     * @return next token (if any can be found)
     */
    @Override
    public Token expectToken(TokenReader tokenReader) {
        final Token token = nextToken(tokenReader);
        if (token == null) {
            throw new IllegalStateException();
        }
        return token;
    }

    /**
     * Takes in a token definition and reads the token from the cursor position.
     * @param token    the token definition
     * @return the token described by the parameter. {@code null} if no such token exists
     */
    @Override
    public String read(Token token) {
        if (token.getStart() < 0) {
            throw new IllegalArgumentException();
        }
        if (token.getEnd() > rest().length()) {
            throw new IllegalArgumentException();
        }
        final String result = processText(rest().substring(0, token.getEnd() + token.getMargin()))
                .substring(0, token.getEnd()).substring(token.getStart());
        cursor += token.getEnd() + token.getMargin();
        return result;
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
            throw new IllegalStateException("Expected pattern '" + pattern.pattern() + "' was not encountered");
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
    public String expect(Pattern pattern) {
        return expect(pattern, false);
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
        snapshots.clear();
        timestamp = System.currentTimeMillis();
        cursor = 0;
        line = 1;
        column = 1;
    }

    /**
     * This method will give the control momentarily to the given {@link SnippetParser} instance.
     * @param parser    the parser to give over the flow to
     */
    @Override
    public <E> E parse(SnippetParser<E> parser) {
        final ReaderSnapshot start = remember();
        int snapshotsRemembered = snapshots.size();
        final E parsed = parser.parse(this);
        //cleaning up after the parser, in case it forgot to remove snapshots it created
        while (snapshots.size() > snapshotsRemembered) {
            forget();
        }
        //if there are now less snapshots than what we recall, we have a problem
        if (snapshots.size() < snapshotsRemembered) {
            throw new IllegalStateException();
        }
        //if the last remembered position does not match our marker, we have a problem
        if (forget() != start) {
            throw new IllegalStateException();
        }
        //Also, the cursor should not have moved backwards in the document
        if (cursor < start.getCursorPosition()) {
            throw new IllegalStateException();
        }
        return parsed;
    }

    /**
     * Peeks ahead for the specified length of characters
     * @param length    the number of characters to read
     * @return the string with the specified length
     */
    @Override
    public String peek(int length) {
        if (length <= 0) {
            throw new IllegalArgumentException();
        }
        final String rest = rest();
        if (length > rest.length()) {
            throw new IllegalArgumentException();
        }
        return rest.substring(0, length);
    }

    /**
     * Remembers the current position in the document, returning the corresponding snapshot.
     * @return the snapshot representing the current position of the document reader
     */
    @Override
    public ReaderSnapshot remember() {
        final ReaderSnapshot snapshot = new ReaderSnapshot(this, cursor);
        snapshots.add(snapshot);
        return snapshot;
    }

    /**
     * Forgets the latest remembered position
     */
    @Override
    public ReaderSnapshot forget() {
        if (!snapshots.isEmpty()) {
            final ReaderSnapshot snapshot = snapshots.get(snapshots.size() - 1);
            snapshots.remove(snapshots.size() - 1);
            return snapshot;
        }
        return null;
    }

    /**
     * Rewinds back to the latest remembered position
     */
    @Override
    public void rewind() {
        if (snapshots.isEmpty()) {
            throw new IllegalStateException();
        }
        reposition(forget());
    }

    /**
     * Repositions the reader according to the snapshot
     * @param snapshot    the snapshot to reposition to
     */
    @Override
    public void reposition(ReaderSnapshot snapshot) {
        if (snapshot.getDocumentReader() != this) {
            throw new IllegalArgumentException();
        }
        if (snapshot.getTimestamp() < timestamp) {
            throw new IllegalArgumentException();
        }
        if (snapshot.getCursorPosition() < 0 || snapshot.getCursorPosition() >= document.length()) {
            throw new IllegalArgumentException();
        }
        cursor = snapshot.getCursorPosition();
        line = snapshot.getLine();
        column = snapshot.getColumn();
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
            throw new IllegalStateException();
        }
        cursor -= count;
    }

}
