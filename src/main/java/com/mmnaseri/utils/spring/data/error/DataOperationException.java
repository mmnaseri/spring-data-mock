package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public abstract class DataOperationException extends RepositoryMockException {

    public DataOperationException(String message) {
        super(message);
    }

    public DataOperationException(String message, Throwable cause) {
        super(message, cause);
    }

}
