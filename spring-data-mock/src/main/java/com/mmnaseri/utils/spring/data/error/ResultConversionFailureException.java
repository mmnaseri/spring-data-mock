package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class ResultConversionFailureException extends DataOperationException {

    public ResultConversionFailureException(Throwable cause) {
        super("Failed to retrieve promised result", cause);
    }

}
