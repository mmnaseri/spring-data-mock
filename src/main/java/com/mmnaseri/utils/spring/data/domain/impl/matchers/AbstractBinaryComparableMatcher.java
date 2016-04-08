package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractBinaryComparableMatcher extends AbstractBinaryMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object value, Object first, Object second) {
        if (!(value instanceof Comparable) || !(first instanceof Comparable) || !(second instanceof Comparable)) {
            throw new InvalidArgumentException("Expected values to be comparable: " + parameter.getPath());
        }
        return matches(parameter, (Comparable) value, (Comparable) first, (Comparable) second);
    }

    protected abstract boolean matches(Parameter parameter, Comparable value, Comparable first, Comparable second);

}
