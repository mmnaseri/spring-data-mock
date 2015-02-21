package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

import java.util.Arrays;
import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:56)
 */
public class BetweenMatcher implements Matcher {

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        if (value == null) {
            return false;
        }
        Objects.requireNonNull(parameters[0]);
        Objects.requireNonNull(parameters[1]);
        if (!(value instanceof Comparable) || !(parameters[0] instanceof Comparable) || !(parameters[1] instanceof Comparable)) {
            throw new IllegalArgumentException("Parameters are not comparable");
        }
        if (parameter.isIgnoreCase()) {
            if (value instanceof CharSequence) {
                value = value.toString().toLowerCase();
            }
            if (parameters[0] instanceof CharSequence) {
                parameters[0] = parameters[0].toString().toLowerCase();
            }
            if (parameters[1] instanceof CharSequence) {
                parameters[1] = parameters[1].toString().toLowerCase();
            }
        }
        Arrays.sort(parameters);
        final Comparable from = (Comparable) parameters[0];
        final Comparable to = (Comparable) parameters[1];
        //noinspection unchecked
        return from.compareTo(value) <= 0 && to.compareTo(value) >= 0;
    }

}
