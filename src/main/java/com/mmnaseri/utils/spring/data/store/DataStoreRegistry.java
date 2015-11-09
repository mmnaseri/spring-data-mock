package com.mmnaseri.utils.spring.data.store;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface DataStoreRegistry {

    <E, K extends Serializable> void register(DataStore<K, E> dataStore);

    <E, K extends Serializable> DataStore<K, E> getDataStore(Class<E> entityType);

    boolean has(Class<?> entityType);

}
