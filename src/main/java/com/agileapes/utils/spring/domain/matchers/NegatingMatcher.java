package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Matcher;
import com.agileapes.utils.spring.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 2:50)
 */
public class NegatingMatcher implements Matcher {

    private final Matcher originalMatcher;

    public NegatingMatcher(Matcher originalMatcher) {
        this.originalMatcher = originalMatcher;
    }

    @Override
    public boolean matches(Parameter parameter, Object value, Object... parameters) {
        return !originalMatcher.matches(parameter, value, parameters);
    }

}
