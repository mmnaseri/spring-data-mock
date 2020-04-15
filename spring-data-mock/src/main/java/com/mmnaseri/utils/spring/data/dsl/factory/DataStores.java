package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;

/**
 * Lets us change the data store and the data store registry
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface DataStores extends ResultAdapters {

    /**
     * Tells the builder to use a different registry
     * @param registry    the registry
     * @return the rest of the configuration
     */
    ResultAdapters withDataStores(DataStoreRegistry registry);

    /**
     * Registers a new data store
     * @param dataStore    the data store
     * @param <E>          the entity type
     * @param <K>          the key type
     * @return the rest of the configuration
     */
    <E, K> DataStoresAnd registerDataStore(DataStore<K, E> dataStore);

}
