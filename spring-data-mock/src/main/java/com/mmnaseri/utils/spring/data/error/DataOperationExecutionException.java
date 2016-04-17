package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class DataOperationExecutionException extends DataOperationException {

    public DataOperationExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

}
