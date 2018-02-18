package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Matcher;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * Used for matching operands to a binary operator
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractBinaryMatcher implements Matcher {

    @Override
    public final boolean matches(Parameter parameter, Object value, Object... properties) {
        if (properties.length != 2) {
            throw new InvalidArgumentException("Expected two values to be passed to operator " + parameter.getOperator().getName() + " at " + parameter.getPath());
        }
        return matches(parameter, value, properties[0], properties[1]);
    }

    /**
     * Called to see if two objects match the criteria set by this matcher
     * @param parameter    the parameter against which the matching is being performed
     * @param value        the value bound to the matching
     * @param first        the first operand
     * @param second       the second operand
     * @return {@literal true} if it was a match
     */
    protected abstract boolean matches(Parameter parameter, Object value, Object first, Object second);

    @Override
    public boolean isApplicableTo(Class<?> parameterType, Class<?>... propertiesTypes) {
        return propertiesTypes.length == 2
                && parameterType.isAssignableFrom(propertiesTypes[0])
                && parameterType.isAssignableFrom(propertiesTypes[1]);
    }
}
