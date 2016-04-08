package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class FunctionNotFoundException extends DataOperationException {

    public FunctionNotFoundException(String functionName) {
        super("No function with this name has been registered: " + functionName);
    }

}
