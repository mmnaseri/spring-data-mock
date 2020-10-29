package com.mmnaseri.utils.spring.data.domain.impl.matchers;

/**
 * This will check to see if the value on the object is less than the argument being passed (the pivot).
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsLessThanMatcher extends AbstractSimpleComparableMatcher {

    @Override
    protected boolean matches(Comparable actual, Comparable pivot) {
        //noinspection unchecked
        return actual != null && pivot != null && pivot.compareTo(actual) > 0;
    }

}
