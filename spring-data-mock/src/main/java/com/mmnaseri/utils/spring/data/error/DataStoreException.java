package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class DataStoreException extends RepositoryMockException {

    public DataStoreException(Class<?> entityType, String message) {
        super(entityType + ": " + message);
    }

}
