package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.OperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.*;

import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class DefaultOperatorContext implements OperatorContext {

    private final Set<Operator> operators;

    public DefaultOperatorContext() {
        operators = new CopyOnWriteArraySet<Operator>();
        operators.add(new ImmutableOperator("AFTER", 1, new IsAfterMatcher(), "After", "IsAfter"));
        operators.add(new ImmutableOperator("BEFORE", 1, new IsBeforeMatcher(), "Before", "IsBefore"));
        operators.add(new ImmutableOperator("CONTAINING", 1, new ContainingMatcher(), "Containing", "IsContaining", "Contains"));
        operators.add(new ImmutableOperator("BETWEEN", 2, new IsBetweenMatcher(), "Between", "IsBetween"));
        operators.add(new ImmutableOperator("NOT_BETWEEN", 2, new IsNotBetweenMatcher(), "NotBetween", "IsNotBetween"));
        operators.add(new ImmutableOperator("ENDING_WITH", 1, new EndingWithMatcher(), "EndingWith", "IsEndingWith", "EndsWith"));
        operators.add(new ImmutableOperator("FALSE", 0, new IsFalseMatcher(), "False", "IsFalse"));
        operators.add(new ImmutableOperator("GREATER_THAN", 1, new IsGreaterThanMatcher(), "GreaterThan", "IsGreaterThan"));
        operators.add(new ImmutableOperator("GREATER_THAN_EQUALS", 1, new IsGreaterThanOrEqualToMatcher(), "GreaterThanEqual", "IsGreaterThanEqual"));
        operators.add(new ImmutableOperator("IN", 1, new IsInMatcher(), "In", "IsIn"));
        operators.add(new ImmutableOperator("IS", 1, new IsEqualToMatcher(), "Is", "EqualTo", "IsEqualTo", "Equals"));
        operators.add(new ImmutableOperator("NOT_NULL", 0, new IsNotNullMatcher(), "NotNull", "IsNotNull"));
        operators.add(new ImmutableOperator("NULL", 0, new IsNullMatcher(), "Null", "IsNull"));
        operators.add(new ImmutableOperator("LESS_THAN", 1, new IsLessThanMatcher(), "LessThan", "IsLessThan"));
        operators.add(new ImmutableOperator("LESS_THAN_EQUAL", 1, new IsLessThanOrEqualToMatcher(), "LessThanEqual", "IsLessThanEqual"));
        operators.add(new ImmutableOperator("LIKE", 1, new IsLikeMatcher(), "Like", "IsLike"));
        operators.add(new ImmutableOperator("NEAR", 1, null, "Near", "IsNear"));
        operators.add(new ImmutableOperator("NOT", 1, new IsNotMatcher(), "IsNot", "Not", "IsNotEqualTo", "DoesNotEqual"));
        operators.add(new ImmutableOperator("NOT_IN", 1, new IsNotInMatcher(), "NotIn", "IsNotIn"));
        operators.add(new ImmutableOperator("NOT_LIKE", 1, new IsNotLikeMatcher(), "NotLike", "IsNotLike"));
        operators.add(new ImmutableOperator("REGEX", 1, new RegexMatcher(), "Regex", "MatchesRegex", "Matches"));
        operators.add(new ImmutableOperator("STARTING_WITH", 1, new StartingWithMatcher(), "StartingWith", "IsStartingWith", "StartsWith"));
        operators.add(new ImmutableOperator("TRUE", 0, new IsTrueMatcher(), "True", "IsTrue"));
    }

    @Override
    public void register(Operator operator) {
        for (Operator item : operators) {
            for (String token : item.getTokens()) {
                for (String newToken : operator.getTokens()) {
                    if (newToken.equals(token)) {
                        throw new IllegalArgumentException("Another operator (" + item.name() + ") already defines answers to this token: " + token);
                    }
                }
            }
        }
        operators.add(operator);
    }

    @Override
    public Operator getBySuffix(String suffix) {
        for (Operator operator : operators) {
            for (String token : operator.getTokens()) {
                if (token.equals(suffix)) {
                    return operator;
                }
            }
        }
        return null;
    }

}
