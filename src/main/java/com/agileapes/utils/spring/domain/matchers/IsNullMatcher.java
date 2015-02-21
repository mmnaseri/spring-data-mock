package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 3:08)
 */
public class IsNullMatcher implements Matcher {

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        return value == null;
    }

}
