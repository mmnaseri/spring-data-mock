package com.mmnaseri.utils.spring.data.dsl.mock;

/**
 * Lets us specify additional implementation classes
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface ImplementationAnd extends End {

    /**
     * Tells the builder to use this additional implementation
     *
     * @param implementation the implementation
     * @return the rest of the configuration
     */
    ImplementationAnd and(Class<?> implementation);

}
