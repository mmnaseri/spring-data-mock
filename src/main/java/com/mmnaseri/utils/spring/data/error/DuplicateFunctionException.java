package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DuplicateFunctionException extends FunctionRegistryException {

    public DuplicateFunctionException(String name) {
        super("Another function with this name has already been registered: " + name);
    }

}
