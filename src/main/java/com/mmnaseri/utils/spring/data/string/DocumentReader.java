package com.mmnaseri.utils.spring.data.string;

import com.mmnaseri.utils.spring.data.string.impl.ReaderSnapshot;

import java.util.Set;
import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public interface DocumentReader {

    /**
     * This method will add a token reader to the document reader so that tokens can be read more easily
     * @param reader    the reader to be registered with the document reader
     */
    void addReader(TokenReader reader);

    /**
     * This should give report as to where the reader stands within the document at this moment
     * @return the part of document currently read through
     */
    String taken();

    /**
     * This will return the parts of document not yet read through by the reader
     * @return the part of the document not processed by the reader
     */
    String rest();

    /**
     * This method will skip all the characters matching the pattern from this point in the document
     * onwards (if any)
     * @param pattern the pattern to skip by
     */
    void skip(Pattern pattern);

    /**
     * This method will determine whether the indicated pattern can be found at this point in the document or not
     * @param pattern    the lookup pattern
     * @return <code>true</code> if the pattern can be found
     */
    boolean has(Pattern pattern);

    /**
     * This method will determine whether the indicated pattern can be found at this point in the document or not
     * @param pattern    the lookup pattern
     * @return <code>true</code> if the pattern can be found
     */
    boolean has(String pattern);

    /**
     * This method will determine if another token can be found in the document
     * @return <code>true</code> if a valid token exists to be read
     */
    boolean hasTokens();

    /**
     * This method will determine if another token can be found in the document
     * @param tokenReader    the token designator for discovering tokens
     * @return <code>true</code> if a token that can be read by the given reader exists in the document
     */
    boolean hasTokens(TokenReader tokenReader);

    /**
     * Determines whether we have hit the end of the document or not
     * @return <code>true</code> if we have no more to go
     */
    boolean hasMore();

    /**
     * Will return the next character
     * @return next character in the document stream
     */
    char nextChar();

    /**
     * Will determine possible tokens that can be read from the document
     * @return a set of all tokens that could be read from the document
     */
    Set<Token> nextToken();

    /**
     * Will give the next token. If no tokens can be found, the method will either take further action
     * to fix this, or throw an Exception. You can confer {@link #hasTokens} to see if another token exists
     * in the document.<br>
     * <strong>NB</strong> This method will attempt to read the <em>first</em> identifiable token, meaning
     * that if one token is the prefix of another, then the first will be discovered.
     *
     * @param tokenReader       the token designator
     * @return next token (if any can be found)
     */
    Token nextToken(TokenReader tokenReader);

    /**
     * Works the same as {@link #nextToken(TokenReader)} with the exception that if the token cannot be read,
     * an exception will be thrown
     *
     * @param tokenReader       the token designator
     * @return next token (if any can be found)
     */
    Token expectToken(TokenReader tokenReader);

    /**
     * Takes in a token definition and reads the token from the cursor position.
     * @param token    the token definition
     * @return the token described by the parameter. {@code null} if no such token exists
     */
    String read(Token token);

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
    String read(Pattern pattern, boolean skipWhitespaces);

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
    String read(String pattern, boolean skipWhitespaces);

    /**
     * Will attempt to read the string matching the given parameter. If the string matched with this pattern
     * does not start at the current point in the document, the result will be considered to be negative.
     * @param pattern            the compiled pattern to be matched against
     * @return the string read by the method, or <code>null</code> if it cannot be found
     * @see Pattern#compile(String)
     * @see Pattern#compile(String, int)
     */
    String read(Pattern pattern);

    /**
     * Will attempt to read the string matching the given parameter. If the string matched with this pattern
     * does not start at the current point in the document, the result will be considered to be negative.
     * @param pattern            the compiled pattern to be matched against
     * @return the string read by the method, or <code>null</code> if it cannot be found
     * @see Pattern#compile(String)
     * @see Pattern#compile(String, int)
     */
    String read(String pattern);

    /**
     * This will attempt to read string matching the given pattern from the document at the current point
     * indicated by the cursor. If failed to do so, the method will be expected to throw an exception or take corrective measures.
     * @param pattern            the regular to query for
     * @param skipWhitespaces    will cause a call to {@link #skip(java.util.regex.Pattern)} with whitespace pattern
     *                           before going forth
     * @return the read string
     */
    String expect(Pattern pattern, boolean skipWhitespaces);

    /**
     * This will attempt to read string matching the given pattern from the document at the current point
     * indicated by the cursor. If failed to do so, the method will be expected to throw an exception or take corrective measures.
     * @param pattern            the regular to query for
     * @return the read string
     */
    String expect(Pattern pattern);

    /**
     * This will attempt to read string matching the given pattern from the document at the current point
     * indicated by the cursor. If failed to do so, the method will be expected to throw an exception or take corrective measures.
     * @param pattern            the regular to query for
     * @return the read string
     */
    String expect(String pattern);

    /**
     * This will attempt to read string matching the given pattern from the document at the current point
     * indicated by the cursor. If failed to do so, the method will be expected to throw an exception or take corrective measures.
     * @param pattern            the regular to query for
     * @param skipWhitespaces    will cause a call to {@link #skip(java.util.regex.Pattern)} with whitespace pattern
     *                           before going forth
     * @return the read string
     */
    String expect(String pattern, boolean skipWhitespaces);

    /**
     * This will cause the state of the reading to be reset. The cursor will be set back to the beginning of the document,
     * and the line/column positioning data will be reset to their initial value.
     */
    void reset();

    /**
     * This method will give the control momentarily to the given {@link SnippetParser} instance.
     * @param parser    the parser to give over the flow to
     * @param <E>       the type of the snippet
     * @return the data returned by the parser
     */
    <E> E parse(SnippetParser<E> parser);

    /**
     * Peeks ahead for the specified length of characters
     * @param length    the number of characters to read
     * @return the string with the specified length
     */
    String peek(int length);

    /**
     * Remembers the current position in the document, returning the corresponding snapshot.
     * @return the snapshot representing the current position of the document reader
     */
    ReaderSnapshot remember();

    /**
     * Forgets the latest remembered position
     * @return the state being forgotten
     */
    ReaderSnapshot forget();

    /**
     * Rewinds back to the latest remembered position
     */
    void rewind();

    /**
     * Repositions the reader according to the snapshot
     * @param snapshot    the snapshot to reposition to
     */
    void reposition(ReaderSnapshot snapshot);

    /**
     * @return the current line of the document at which the reader stands. Starts at 1.
     */
    int getLine();

    /**
     * @return the column in the current line to which the reader has advanced. Starts at 1.
     */
    int getColumn();

    /**
     * Moves back the specified number of characters
     * @param count character count to backtrack by
     */
    void backtrack(int count);

}
