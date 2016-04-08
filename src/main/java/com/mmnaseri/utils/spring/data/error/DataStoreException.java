package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class DataStoreException extends RepositoryMockException {

    private final Class<?> entityType;

    public DataStoreException(Class<?> entityType, String message) {
        super(message);
        this.entityType = entityType;
    }

    public DataStoreException(Class<?> entityType, String message, Throwable cause) {
        super(message, cause);
        this.entityType = entityType;
    }

}
