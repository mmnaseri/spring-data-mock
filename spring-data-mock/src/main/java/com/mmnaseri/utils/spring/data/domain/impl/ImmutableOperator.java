package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Matcher;
import com.mmnaseri.utils.spring.data.domain.Operator;

/**
 * This is an immutable operator.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class ImmutableOperator implements Operator {

    private final String name;
    private final int operands;
    private final Matcher matcher;
    private final String[] tokens;

    public ImmutableOperator(String name, int operands, Matcher matcher, String... tokens) {
        this.name = name;
        this.operands = operands;
        this.matcher = matcher;
        this.tokens = tokens;
    }

    public String getName() {
        return name;
    }

    @Override
    public int getOperands() {
        return operands;
    }

    @Override
    public Matcher getMatcher() {
        return matcher;
    }

    @Override
    public String[] getTokens() {
        return tokens;
    }

    @Override
    public String toString() {
        return name;
    }

}
