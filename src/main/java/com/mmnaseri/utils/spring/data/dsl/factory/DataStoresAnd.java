package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.store.DataStore;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface DataStoresAnd extends ResultAdapters {

    <E, K extends Serializable> DataStoresAnd and(DataStore<K, E> dataStore);

}
