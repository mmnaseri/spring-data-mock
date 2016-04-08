package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Matcher;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractStateMatcher implements Matcher {

    @Override
    public boolean matches(Parameter parameter, Object value, Object... properties) {
        if (properties.length != 0) {
            throw new InvalidArgumentException("This operator does not take any operands: " + parameter.getOperator().getName());
        }
        return matches(parameter, value);
    }

    protected abstract boolean matches(Parameter parameter, Object value);

}
