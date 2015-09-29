package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsNullMatcher extends AbstractStateMatcher {
    @Override
    protected boolean matches(Parameter parameter, Object value) {
        return value == null;
    }
}
