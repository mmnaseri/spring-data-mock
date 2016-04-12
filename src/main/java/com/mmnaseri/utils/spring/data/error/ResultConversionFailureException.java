package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class ResultConversionFailureException extends DataOperationException {

    public ResultConversionFailureException(Throwable cause) {
        super("Failed to retrieve promised result", cause);
    }

}
