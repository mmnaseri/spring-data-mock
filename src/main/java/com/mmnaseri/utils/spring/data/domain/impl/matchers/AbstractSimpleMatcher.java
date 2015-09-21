package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Matcher;
import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public abstract class AbstractSimpleMatcher implements Matcher {

    @Override
    public boolean matches(Parameter parameter, Object value, Object... properties) {
        if (properties.length != 1) {
            throw new IllegalStateException("Expected exactly one parameter to be passed down");
        }
        return matches(parameter, value, properties[0]);
    }

    protected abstract boolean matches(Parameter parameter, Object actual, Object expected);

}
