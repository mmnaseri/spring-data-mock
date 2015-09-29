package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Matcher;
import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractBinaryMatcher implements Matcher {

    @Override
    public boolean matches(Parameter parameter, Object value, Object... properties) {
        if (properties.length != 2) {
            throw new IllegalArgumentException("Expected two values to be passed to operator " + parameter.getOperator().name());
        }
        return matches(parameter, value, properties[0], properties[1]);
    }

    protected abstract boolean matches(Parameter parameter, Object value, Object first, Object second);


}
