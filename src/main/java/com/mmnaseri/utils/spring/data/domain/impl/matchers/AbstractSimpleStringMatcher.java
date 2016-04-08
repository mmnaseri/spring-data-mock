package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractSimpleStringMatcher extends AbstractSimpleMatcher {

    @Override
    protected boolean matches(Parameter parameter, Object actual, Object expected) {
        if (!(actual instanceof String) || !(expected instanceof String)) {
            throw new InvalidArgumentException("Expected string values for property: " + parameter.getPath());
        }
        return matches(parameter, (String) actual, (String) expected);
    }

    protected abstract boolean matches(Parameter parameter, String actual, String argument);

}
