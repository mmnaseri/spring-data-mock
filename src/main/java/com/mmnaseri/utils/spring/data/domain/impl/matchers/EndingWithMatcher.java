package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class EndingWithMatcher extends AbstractSimpleStringMatcher {

    @Override
    protected boolean matches(Parameter parameter, String actual, String argument) {
        return actual != null && argument != null && actual.toLowerCase().endsWith(argument.toLowerCase());
    }

}
