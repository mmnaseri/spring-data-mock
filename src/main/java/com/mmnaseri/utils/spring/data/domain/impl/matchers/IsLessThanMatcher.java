package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsLessThanMatcher extends AbstractSimpleComparableMatcher {

    @Override
    protected boolean matches(Parameter parameter, Comparable actual, Comparable pivot) {
        //noinspection unchecked
        return actual != null && pivot != null && pivot.compareTo(actual) > 0;
    }

}
