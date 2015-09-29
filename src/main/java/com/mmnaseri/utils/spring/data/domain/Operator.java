package com.mmnaseri.utils.spring.data.domain;

import com.mmnaseri.utils.spring.data.domain.impl.matchers.EqualToMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsNotMatcher;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public enum Operator {

    AFTER(1, null, "After", "IsAfter"),
    BEFORE(1, null, "Before", "IsBefore"),
    CONTAINING(1, null, "Containing", "IsContaining", "Contains"),
    BETWEEN(2, null, "Between", "IsBetween"),
    ENDING_WITH(1, null, "EndingWith", "IsEndingWith", "EndsWith"),
    EXISTS(1, null, "Exists"),
    FALSE(0, null, "False", "IsFalse"),
    GREATER_THAN(1, null, "GreaterThan", "IsGreaterThan"),
    GREATER_THAN_EQUALS(1, null, "GreaterThanEqual", "IsGreaterThanEqual"),
    IN(1, null, "In", "IsIn"),
    IS(1, new EqualToMatcher(), "Is", "EqualTo", "IsEqualTo", "Equals"),
    IS_NOT_NULL(0, null, "NotNull", "IsNotNull"),
    IS_NULL(0, null, "Null", "IsNull"),
    LESS_THAN(1, null, "LessThan", "IsLessThan"),
    LESS_THAN_EQUAL(1, null, "LessThanEqual", "IsLessThanEqual"),
    LIKE(1, null, "Like", "IsLike"),
    NEAR(1, null, "Near", "IsNear"),
    NOT(1, new IsNotMatcher(), "IsNot", "Not", "IsNotEqualTo", "DoesNotEqual"),
    NOT_IN(1, null, "NotIn", "IsNotIn"),
    NOT_LIKE(1, null, "NotLike", "IsNotLike"),
    REGEX(1, null, "Regex", "MatchesRegex", "Matches"),
    STARTING_WITH(1, null, "StartingWith", "IsStartingWith", "StartsWith"),
    TRUE(0, null, "True", "IsTrue"),
    WITHIN(1, null, "Within", "IsWithin")
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
