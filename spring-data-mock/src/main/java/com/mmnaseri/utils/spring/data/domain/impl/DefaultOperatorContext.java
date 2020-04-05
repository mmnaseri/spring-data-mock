package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.OperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.ContainingMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.EndingWithMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsBetweenMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsEqualToMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsFalseMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsGreaterThanMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsGreaterThanOrEqualToMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsInMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsLessThanMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsLessThanOrEqualToMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsLikeMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsNotBetweenMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsNotInMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsNotLikeMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsNotMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsNotNullMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsNullMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsTrueMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.RegexMatcher;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.StartingWithMatcher;
import com.mmnaseri.utils.spring.data.error.DuplicateOperatorException;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Arrays;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * This class is used to store the operators used in the name of a query method. Operators are matched by
 * the "suffixes" eagerly (meaning that "EqualTo" will precede over "To").
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public class DefaultOperatorContext implements OperatorContext {

    private static final Log log = LogFactory.getLog(DefaultOperatorContext.class);
    private final Set<Operator> operators;

    public DefaultOperatorContext() {
        this(true);
    }

    public DefaultOperatorContext(boolean registerDefaults) {
        operators = new CopyOnWriteArraySet<>();
        if (registerDefaults) {
            log.info("Registering all the default operators");
            operators.add(new ImmutableOperator("AFTER", 1, new IsGreaterThanMatcher(), "After", "IsAfter"));
            operators.add(new ImmutableOperator("BEFORE", 1, new IsLessThanMatcher(), "Before", "IsBefore"));
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
    }

    @Override
    public void register(Operator operator) {
        log.info("Registering operator " + operator.getName() + " which will respond to suffixes " + Arrays.toString(operator.getTokens()));
        for (Operator item : operators) {
            for (String token : item.getTokens()) {
                for (String newToken : operator.getTokens()) {
                    if (newToken.equals(token)) {
                        throw new DuplicateOperatorException(item, token);
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
