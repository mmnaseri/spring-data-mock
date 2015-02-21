package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:51)
 */
public abstract class AbstractComparingMatcher implements Matcher {

    public abstract int compareFirstToSecond();

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        if (value == null) {
            return false;
        }
        Objects.requireNonNull(parameters[0]);
        if (parameters[0] instanceof Comparable && value instanceof Comparable) {
            Comparable comparable = (Comparable) parameters[0];
            if (parameter.isIgnoreCase()) {
                if (value instanceof CharSequence) {
                    value = value.toString().toLowerCase();
                }
                if (comparable instanceof CharSequence) {
                    comparable = comparable.toString().toLowerCase();
                }
                parameters[0] = parameters[0].toString().toLowerCase();
            }
            if (value.getClass().isAssignableFrom(comparable.getClass())) {
                //noinspection unchecked
                final int comparison = comparable.compareTo(value);
                final int compareFirstToSecond = compareFirstToSecond();
                if (compareFirstToSecond == 0) {
                    return comparison == 0;
                } else {
                    return comparison * compareFirstToSecond() > 0;
                }
            } else {
                throw new IllegalArgumentException("Parameter types do not match");
            }
        }
        throw new IllegalArgumentException("Parameters are not comparable");
    }

}
