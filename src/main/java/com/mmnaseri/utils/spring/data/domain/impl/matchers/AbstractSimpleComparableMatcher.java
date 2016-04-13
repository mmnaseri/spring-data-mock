package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * This is used to compare two items. One being the value, and the other the sole parameter.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractSimpleComparableMatcher extends AbstractSimpleMatcher {

    @Override
    protected final boolean matches(Parameter parameter, Object actual, Object expected) {
        if (!(actual instanceof Comparable) || !(expected instanceof Comparable)) {
            throw new InvalidArgumentException("Expected property to be comparable: " + parameter.getPath());
        }
        return matches(parameter, (Comparable) actual, (Comparable) expected);
    }

    /**
     * Does comparison and returns the result.
     * @param parameter    the parameter
     * @param actual       the actual value
     * @param pivot        the pivot
     * @return {@literal true} if the match succeeded
     */
    protected abstract boolean matches(Parameter parameter, Comparable actual, Comparable pivot);

}
