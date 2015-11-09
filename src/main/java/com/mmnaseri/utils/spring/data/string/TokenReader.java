package com.mmnaseri.utils.spring.data.string;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public interface TokenReader {

    /**
     * This method will attempt to read the token this reader recognizes from the text input.
     * If the input does not match the expectations, it is expected that a {@code null} value
     * be returned.
     * @param text    the text input
     * @return the read token or {@code null} if no valid tokens were found
     */
    Token read(String text);

}
