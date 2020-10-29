package com.mmnaseri.utils.spring.data.domain.impl.matchers;

/**
 * This matcher checks to see if the two values are the same, barring case differences.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsLikeMatcher extends AbstractSimpleStringMatcher {

    @Override
    protected boolean matches(String actual, String argument) {
        return actual != null && actual.equalsIgnoreCase(argument);
    }

}
