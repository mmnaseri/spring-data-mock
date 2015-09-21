package com.mmnaseri.utils.spring.data.domain;

import com.mmnaseri.utils.spring.data.domain.impl.matchers.EqualToMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsNotMatcher;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public enum Operator {

    EQUAL_TO(1, new EqualToMatcher(), "Is", "EqualTo", "IsEqualTo", "Equals"),
    IS_NOT(1, new IsNotMatcher(), "IsNot", "Not", "IsNotEqualTo", "DoesNotEqual"),
    ;
    private final int operands;
    private final Matcher matcher;
    private final String[] suffices;

    Operator(int operands, Matcher matcher, String... suffices) {
        this.operands = operands;
        this.matcher = matcher;
        this.suffices = suffices;
    }

    public boolean matches(Parameter parameter, Object value, Object... properties) {
        return matcher.matches(parameter, value, properties);
    }

    public int getOperands() {
        return operands;
    }

    public static Operator getBySuffix(String suffix) {
        for (Operator operator : Operator.values()) {
            for (String candidate : operator.suffices) {
                if (candidate.equals(suffix)) {
                    return operator;
                }
            }
        }
        return null;
    }

}
