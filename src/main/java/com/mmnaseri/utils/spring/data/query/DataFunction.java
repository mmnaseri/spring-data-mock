package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.store.DataStore;

import java.io.Serializable;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface DataFunction<R> {

    <K extends Serializable, E> R apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration configuration, List<E> selection);

}
