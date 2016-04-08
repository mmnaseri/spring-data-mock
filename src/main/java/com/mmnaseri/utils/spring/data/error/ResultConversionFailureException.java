package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class ResultConversionFailureException extends DataOperationException {
    public ResultConversionFailureException(String message) {
        super(message);
    }

    public ResultConversionFailureException(String message, Throwable cause) {
        super(message, cause);
    }
}
