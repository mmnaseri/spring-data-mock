package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.MatchedOperator;
import com.mmnaseri.utils.spring.data.domain.Matcher;
import com.mmnaseri.utils.spring.data.domain.Operator;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/17/16, 12:37 PM)
 */
@SuppressWarnings("WeakerAccess")
public class ImmutableMatchedOperator implements MatchedOperator {

    private final Operator original;
    private final String matchedToken;

    public ImmutableMatchedOperator(Operator original, String matchedToken) {
        this.original = original;
        this.matchedToken = matchedToken;
    }

    @Override
    public String getName() {
        return original.getName();
    }

    @Override
    public int getOperands() {
        return original.getOperands();
    }

    @Override
    public Matcher getMatcher() {
        return original.getMatcher();
    }

    @Override
    public String[] getTokens() {
        return original.getTokens();
    }

    @Override
    public String getMatchedToken() {
        return matchedToken;
    }

}
