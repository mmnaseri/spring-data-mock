package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 3:09)
 */
public abstract class AbstractTypedMatcher implements Matcher {

    private final Class<?> superType;
    private final int parameters;

    public AbstractTypedMatcher(Class<?> superType, int parameters) {
        this.superType = superType;
        this.parameters = parameters;
    }

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        if (value == null) {
            return false;
        }
        if (!superType.isInstance(value)) {
            throw new IllegalArgumentException("Parameter not of the expected type: " + superType);
        }
        for (int i = 0; i < this.parameters; i ++) {
            Objects.requireNonNull(parameters[i]);
            if (!superType.isInstance(parameters[i])) {
                throw new IllegalArgumentException("Parameter not of the expected type: " + superType);
            }
        }
        return doMatch(parameter, value, parameters);
    }

    protected abstract boolean doMatch(Parameter parameter, Object value, Object[] parameters);
}
