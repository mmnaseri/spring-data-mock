package com.mmnaseri.utils.spring.data.domain;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface Operator {

    String getName();

    int getOperands();

    Matcher getMatcher();

    String[] getTokens();

}
