package com.mmnaseri.utils.spring.data.string;

import java.util.regex.Pattern;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public interface DocumentReader {

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
     * Determines whether we have hit the end of the document or not
     * @return <code>true</code> if we have no more to go
     */
    boolean hasMore();

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
