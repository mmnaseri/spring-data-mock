package com.mmnaseri.utils.spring.data.store;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public interface DataStoreEvent {

    RepositoryMetadata getRepositoryMetadata();

    DataStore<?, ?> getDataStore();

}
