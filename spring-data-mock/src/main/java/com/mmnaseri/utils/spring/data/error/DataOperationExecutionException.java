package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DataOperationExecutionException extends DataOperationException {

    public DataOperationExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
