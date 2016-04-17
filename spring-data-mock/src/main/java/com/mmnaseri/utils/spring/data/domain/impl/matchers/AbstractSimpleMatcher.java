package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Matcher;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * This matcher is used to determine if a condition holds for a single parameter
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public abstract class AbstractSimpleMatcher implements Matcher {

    @Override
    public final boolean matches(Parameter parameter, Object value, Object... properties) {
        if (properties.length != 1) {
            throw new InvalidArgumentException("Expected exactly one parameter to be passed down: " + parameter.getPath());
        }
        return matches(parameter, value, properties[0]);
    }

    /**
     * Called to see if the condition holds
     * @param parameter the parameter
     * @param actual    the actual value
     * @param expected  the expectation
     * @return {@literal true} if the condition holds
     */
    protected abstract boolean matches(Parameter parameter, Object actual, Object expected);

}
