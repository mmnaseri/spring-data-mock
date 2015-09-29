package com.mmnaseri.utils.spring.data.domain;

import com.mmnaseri.utils.spring.data.domain.impl.matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public enum Operator {

    AFTER(1, new IsAfterMatcher(), "After", "IsAfter"),
    BEFORE(1, new IsBeforeMatcher(), "Before", "IsBefore"),
    CONTAINING(1, new ContainingMatcher(), "Containing", "IsContaining", "Contains"),
    BETWEEN(2, new IsBetweenMatcher(), "Between", "IsBetween"),
    NOT_BETWEEN(2, new IsNotBetweenMatcher(), "NotBetween", "IsNotBetween"),
    ENDING_WITH(1, new EndingWithMatcher(), "EndingWith", "IsEndingWith", "EndsWith"),
//    EXISTS(1, null, "Exists"),
    FALSE(0, new IsFalseMatcher(), "False", "IsFalse"),
    GREATER_THAN(1, new IsGreaterThanMatcher(), "GreaterThan", "IsGreaterThan"),
    GREATER_THAN_EQUALS(1, new IsGreaterThanOrEqualToMatcher(), "GreaterThanEqual", "IsGreaterThanEqual"),
    IN(1, new IsInMatcher(), "In", "IsIn"),
    IS(1, new IsEqualToMatcher(), "Is", "EqualTo", "IsEqualTo", "Equals"),
    IS_NOT_NULL(0, new IsNotNullMatcher(), "NotNull", "IsNotNull"),
    IS_NULL(0, new IsNullMatcher(), "Null", "IsNull"),
    LESS_THAN(1, new IsLessThanMatcher(), "LessThan", "IsLessThan"),
    LESS_THAN_EQUAL(1, new IsLessThanOrEqualToMatcher(), "LessThanEqual", "IsLessThanEqual"),
    LIKE(1, new IsLikeMatcher(), "Like", "IsLike"),
    NEAR(1, null, "Near", "IsNear"),
    NOT(1, new IsNotMatcher(), "IsNot", "Not", "IsNotEqualTo", "DoesNotEqual"),
    NOT_IN(1, new IsNotInMatcher(), "NotIn", "IsNotIn"),
    NOT_LIKE(1, new IsNotLikeMatcher(), "NotLike", "IsNotLike"),
    REGEX(1, new RegexMatcher(), "Regex", "MatchesRegex", "Matches"),
    STARTING_WITH(1, new StartingWithMatcher(), "StartingWith", "IsStartingWith", "StartsWith"),
    TRUE(0, new IsTrueMatcher(), "True", "IsTrue"),
//    WITHIN(1, null, "Within", "IsWithin")
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
