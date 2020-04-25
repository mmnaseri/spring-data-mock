package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class DataStoreNotFoundException extends DataStoreException {

    public DataStoreNotFoundException(Class<?> entityType) {
        super(entityType, "No data store could be found for this entity type: " + entityType);
    }

}
