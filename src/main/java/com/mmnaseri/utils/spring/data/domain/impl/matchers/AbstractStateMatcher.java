package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Matcher;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * This is used when there is no argument needed to determine the validity of the match
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractStateMatcher implements Matcher {

    @Override
    public final boolean matches(Parameter parameter, Object value, Object... properties) {
        if (properties.length != 0) {
            throw new InvalidArgumentException("This operator does not take any operands: " + parameter.getOperator().getName() + " at " + parameter.getPath());
        }
        return matches(parameter, value);
    }

    /**
     * Called to determine the match
     * @param parameter    the parameter
     * @param value        the expected value
     * @return {@literal true} if the match applies
     */
    protected abstract boolean matches(Parameter parameter, Object value);

}
