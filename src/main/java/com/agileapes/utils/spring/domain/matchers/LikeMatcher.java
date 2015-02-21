package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:42)
 */
public class LikeMatcher implements Matcher {

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        return value == null && parameters[0] == null || (value instanceof Character || value instanceof CharSequence) && value.toString().toLowerCase().contains(parameters[0].toString().toLowerCase());
    }

}
