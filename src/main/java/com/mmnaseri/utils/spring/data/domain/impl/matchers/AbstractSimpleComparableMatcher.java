package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractSimpleComparableMatcher extends AbstractSimpleMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object actual, Object expected) {
        if (!(actual instanceof Comparable) || !(expected instanceof Comparable)) {
            throw new InvalidArgumentException("Expected property to be comparable: " + parameter.getPath());
        }
        return matches(parameter, (Comparable) actual, (Comparable) expected);
    }

    protected abstract boolean matches(Parameter parameter, Comparable actual, Comparable pivot);

}
