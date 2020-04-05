package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.store.DataStore;

/**
 * Lets us add another data store
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface DataStoresAnd extends ResultAdapters {

    /**
     * Adds another data store
     * @param dataStore    the data store
     * @param <E>          the type of the entity
     * @param <K>          the type of the key
     * @return the rest of the configuration
     */
    <E, K> DataStoresAnd and(DataStore<K, E> dataStore);

}
