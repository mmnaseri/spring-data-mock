package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface encapsulates the responsibility of matching a given entity to a set of parameters based on
 * the operation defined for that parameter.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public interface Matcher {

    /**
     * Determines if the value matches the properties or not, based on the underlying operation.
     * @param parameter     the parameter for which the match is happening.
     * @param value         the value being matched against.
     * @param properties    the properties for the invocation
     * @return {@literal true} if it was a match
     */
    boolean matches(Parameter parameter, Object value, Object... properties);

    boolean isApplicableTo(Class<?> parameterType, Class<?>[] propertiesTypes);
}
