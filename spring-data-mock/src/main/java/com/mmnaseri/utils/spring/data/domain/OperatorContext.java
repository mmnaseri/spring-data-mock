package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface is used to define a context holding definitions for operators.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface OperatorContext {

    /**
     * @param operator registers a new operator, taking care to avoid registration of new operators that override a
     *                 predefined operator suffix.
     */
    void register(Operator operator);

    /**
     * Finds the operator that matches the given suffix
     *
     * @param suffix the suffix to look for
     * @return the operator that matches the suffix or {@literal null} if none matches.
     */
    Operator getBySuffix(String suffix);

}
