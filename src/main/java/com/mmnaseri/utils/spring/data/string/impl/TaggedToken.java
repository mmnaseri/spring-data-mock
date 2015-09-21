package com.mmnaseri.utils.spring.data.string.impl;

import com.mmnaseri.utils.spring.data.string.Token;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public class TaggedToken implements Token {

    private final Token token;
    private final int tag;

    public TaggedToken(Token token) {
        this(token, NO_TAG);
    }

    public TaggedToken(Token token, int tag) {
        this.token = token;
        this.tag = tag;
    }

    @Override
    public int getTag() {
        return tag;
    }

    @Override
    public int getStart() {
        return token.getStart();
    }

    @Override
    public int getEnd() {
        return token.getEnd();
    }

    @Override
    public int getMargin() {
        return token.getMargin();
    }

    @Override
    public int getLength() {
        return token.getLength();
    }

    @Override
    public boolean isTagged() {
        return tag != NO_TAG;
    }

}
