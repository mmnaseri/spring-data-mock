package com.agileapes.utils.spring.domain.matchers;

import com.agileapes.utils.spring.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/24 AD, 3:09)
 */
public class StartingWithMatcher extends AbstractTypedMatcher {

    public StartingWithMatcher() {
        super(CharSequence.class, 1);
    }

    @Override
    protected boolean doMatch(Parameter parameter, Object value, Object[] parameters) {
        if (parameter.isIgnoreCase()) {
            value = value.toString().toLowerCase();
            parameters[0] = parameters[0].toString().toLowerCase();
        }
        return value.toString().startsWith(parameters[0].toString());
    }

}
