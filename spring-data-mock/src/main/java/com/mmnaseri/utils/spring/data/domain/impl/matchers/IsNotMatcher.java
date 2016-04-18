package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

import java.util.Objects;

/**
 * This matcher will determine if the two values are not equal.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public class IsNotMatcher extends AbstractSimpleMatcher {
    @Override
    protected boolean matches(Parameter parameter, Object actual, Object expected) {
        return !Objects.equals(actual, expected);
    }
}
