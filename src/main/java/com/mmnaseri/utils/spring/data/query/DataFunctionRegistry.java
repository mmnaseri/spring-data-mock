package com.mmnaseri.utils.spring.data.query;

import java.util.Set;

/**
 * A data function registry lets you register and maintain a set of data functions by name.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface DataFunctionRegistry {

    /**
     * Registers a new data function
     * @param name        the name under which the function will be known
     * @param function    the actual function
     */
    void register(String name, DataFunction<?> function);

    /**
     * Finds the function associated with the given name. If a function with that name does not exist,
     * throws an exception.
     * @param name    the name of the function.
     * @return the data function
     * @throws com.mmnaseri.utils.spring.data.error.FunctionNotFoundException
     */
    DataFunction<?> getFunction(String name);

    /**
     * @return the set of all registered functions
     */
    Set<String> getFunctions();

}
