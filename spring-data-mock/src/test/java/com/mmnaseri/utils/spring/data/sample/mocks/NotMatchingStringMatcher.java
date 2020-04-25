package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.AbstractSimpleStringMatcher;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 7:01 PM)
 */
public class NotMatchingStringMatcher extends AbstractSimpleStringMatcher {

    private String actual;
    private String argument;

    @Override
    protected boolean matches(Parameter parameter, String actual, String argument) {
        this.actual = actual;
        this.argument = argument;
        return false;
    }

    public String getActual() {
        return actual;
    }

    public String getArgument() {
        return argument;
    }

}
