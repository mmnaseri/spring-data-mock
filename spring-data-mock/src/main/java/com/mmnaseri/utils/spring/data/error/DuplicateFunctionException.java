package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class DuplicateFunctionException extends FunctionRegistryException {

    public DuplicateFunctionException(String name) {
        super("Another function with this name has already been registered: " + name);
    }

}
