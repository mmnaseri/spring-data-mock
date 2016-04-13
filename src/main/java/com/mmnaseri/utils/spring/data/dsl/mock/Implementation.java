package com.mmnaseri.utils.spring.data.dsl.mock;

/**
 * Lets us specify which implementations the repository should use
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface Implementation extends End {

    /**
     * Tells the builder to use the given implementation
     * @param implementation    the implementation
     * @return the rest of the configuration
     */
    ImplementationAnd usingImplementation(Class<?> implementation);

}
