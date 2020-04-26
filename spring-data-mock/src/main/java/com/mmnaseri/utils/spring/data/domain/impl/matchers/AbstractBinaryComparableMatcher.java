package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * This class is the base class used for doing binary operations when both operands are {@link Comparable Comparable}
 * objects
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractBinaryComparableMatcher extends AbstractBinaryMatcher {

    @Override
    protected final boolean matches(Parameter parameter, Object value, Object first, Object second) {
        if (!(value instanceof Comparable) || !(first instanceof Comparable) || !(second instanceof Comparable)) {
            throw new InvalidArgumentException("Expected values to be comparable: " + parameter.getPath());
        }
        return matches(parameter, (Comparable) value, (Comparable) first, (Comparable) second);
    }

    /**
     * Is called to determine when the two comparable items fit the criteria of this matcher
     *
     * @param parameter the parameter for which the matching is being performed
     * @param value     the value against which the comparison is being performed
     * @param first     the first value
     * @param second    the second value
     * @return {@literal true} if it was a match
     */
    protected abstract boolean matches(Parameter parameter, Comparable value, Comparable first, Comparable second);

}
