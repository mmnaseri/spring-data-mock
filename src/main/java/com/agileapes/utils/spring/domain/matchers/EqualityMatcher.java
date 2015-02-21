package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:40)
 */
public class EqualityMatcher implements Matcher {

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        if (parameter.isIgnoreCase()) {
            value = value.toString().toLowerCase();
            parameters[0] = parameters[0].toString().toLowerCase();
        }
        return value == null && parameters[0] == null || value != null && value.equals(parameters[0]);
    }

}
