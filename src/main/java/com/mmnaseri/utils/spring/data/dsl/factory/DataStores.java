package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface DataStores extends ResultAdapters {

    ResultAdapters withDataStores(DataStoreRegistry registry);

    <E, K extends Serializable> DataStoresAnd registerDataStore(DataStore<K, E> dataStore);

}
