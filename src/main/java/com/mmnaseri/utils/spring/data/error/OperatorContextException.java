package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class OperatorContextException extends RepositoryMockException {
    public OperatorContextException(String message) {
        super(message);
    }

    public OperatorContextException(String message, Throwable cause) {
        super(message, cause);
    }
}
