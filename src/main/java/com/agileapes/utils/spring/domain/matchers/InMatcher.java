package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:44)
 */
public class InMatcher implements Matcher {

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        if (value == null) {
            return false;
        }
        if (parameter.isIgnoreCase() && value instanceof CharSequence) {
            value = value.toString().toLowerCase();
        }
        Objects.requireNonNull(parameters[0]);
        final Object collection = parameters[0];
        if (collection instanceof Iterable) {
            Iterable iterable = (Iterable) parameter;
            for (Object item : iterable) {
                if (parameter.isIgnoreCase() && item instanceof CharSequence) {
                    item = item.toString().toLowerCase();
                }
                if (item.equals(value)) {
                    return true;
                }
            }
        }
        return false;
    }

}
