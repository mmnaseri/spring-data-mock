package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class ResultAdapterFailureException extends DataOperationException {

    public ResultAdapterFailureException(Object originalValue, Class<?> expectedType) {
        super("Could not adapt value: <" + originalValue + "> to type <" + expectedType + ">");
    }

    public ResultAdapterFailureException(Object originalValue, Class<?> expectedType, String failure) {
        super("Could not adapt value: <" + originalValue + "> to type <" + expectedType + ">; " + failure);
    }

    public ResultAdapterFailureException(Object originalValue, Class<?> expectedType, Throwable failure) {
        super("Could not adapt value: <" + originalValue + "> to type <" + expectedType + ">; ", failure);
    }

}
