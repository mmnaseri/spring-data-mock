package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class EqualToMatcher extends AbstractSimpleMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object actual, Object expected) {
        return Objects.equals(actual, expected);
    }

}
