package com.agileapes.utils.spring.domain;

import com.agileapes.utils.spring.domain.matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:39)
 */
public enum Operator {

    EQUAL_TO(1, false, new EqualityMatcher(), "Is", "Equals"),
    NOT(1, false, new NegatingMatcher(new EqualityMatcher()), "Not", "IsNot"),
    LIKE(1, false, new LikeMatcher(), "Like", "IsLike"),
    NOT_LIKE(1, false, new NegatingMatcher(new LikeMatcher()), "NotLike", "IsNotLike"),
    LESS_THAN(1, false, new LessThanMatcher(), "LessThan", "Before", "IsBefore"),
    LESS_THAN_OR_EQUAL_TO(1, false, new NegatingMatcher(new GreaterThanMatcher()), "LessThanOrEqualTo", "LessThanEqual", "IsLessThanOrEqualTo", "IsLessThanEqual"),
    GREATER_THAN(1, false, new GreaterThanMatcher(), "GreaterThan", "After", "IsAfter"),
    GREATER_THAN_OR_EQUAL_TO(1, false, new NegatingMatcher(new LessThanMatcher()), "GreaterThanOrEqualTo", "GreaterThanEqual", "IsGreaterThanOrEqualTo", "IsGreaterThanEqual"),
    BETWEEN(2, false, new BetweenMatcher(), "Between", "IsBetween"),
    IN(1, true, new InMatcher(), "In"),
    NOT_IN(1, true, new NegatingMatcher(new InMatcher()), "NotIn"),
    IS_NULL(0, false, new IsNullMatcher(), "IsNull"),
    IS_NOT_NULL(0, false, new NegatingMatcher(new IsNullMatcher()), "IsNotNull", "NotNull"),
    STARTING_WITH(1, false, new StartingWithMatcher(), "StartingWith", "IsStartingWith", "StartsWith"),
    ENDING_WITH(1, false, new EndingWithMatcher(), "EndingWith", "IsEndingWith", "EndsWith"),
    CONTAINING(1, false, new ContainingMatcher(), "Containing", "Containing", "Contains"),
    IS_TRUE(0, false, new IsTrueMatcher(), "IsTrue", "True"),
    IS_FALSE(0, false, new NegatingMatcher(new IsTrueMatcher()), "IsFalse", "False")
    ;
    private final String[] suffix;
    private final int operands;
    private final boolean collection;
    private final Matcher matcher;

    Operator(int operands, boolean collection, Matcher matcher, String... suffix) {
        this.suffix = suffix;
        this.operands = operands;
        this.collection = collection;
        this.matcher = matcher;
    }

    public String[] getSuffix() {
        return suffix;
    }

    public int getOperands() {
        return operands;
    }

    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        return matcher.matches(parameter, value, parameters);
    }

    public static Operator getBySuffix(String suffix) {
        for (Operator operator : values()) {
            for (String current : operator.getSuffix()) {
                if (current.equals(suffix)) {
                    return operator;
                }
            }
        }
        return null;
    }

    public boolean isCollection() {
        return collection;
    }
}

