package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * This matcher checks to see if the two values are the same, barring case differences.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsLikeMatcher extends AbstractSimpleStringMatcher {

    @Override
    protected boolean matches(Parameter parameter, String actual, String argument) {
        return actual != null && argument != null && actual.equalsIgnoreCase(argument);
    }

}
