package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 3:13)
 */
public class IsTrueMatcher extends AbstractTypedMatcher {

    public IsTrueMatcher() {
        super(Boolean.class, 0);
    }

    @Override
    protected boolean doMatch(Parameter parameter, Object value, Object[] parameters) {
        return Boolean.TRUE.equals(value);
    }

}
