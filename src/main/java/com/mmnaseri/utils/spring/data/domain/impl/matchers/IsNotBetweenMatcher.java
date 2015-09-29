package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsNotBetweenMatcher extends AbstractBinaryComparableMatcher {

    @Override
    protected boolean matches(Parameter parameter, Comparable value, Comparable first, Comparable second) {
        //noinspection unchecked
        return first != null && second != null && (value == null || first.compareTo(value) > 0 || second.compareTo(value) < 0);
    }

}
