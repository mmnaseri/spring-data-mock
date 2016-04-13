package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class FunctionNotFoundException extends FunctionRegistryException {

    public FunctionNotFoundException(String functionName) {
        super("No function with this name has been registered: " + functionName);
    }

}
