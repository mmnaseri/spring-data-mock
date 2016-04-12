package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractDataStoreEvent implements DataStoreEvent {

    private final RepositoryMetadata repositoryMetadata;
    private final DataStore<?, ?> dataStore;

    public AbstractDataStoreEvent(RepositoryMetadata repositoryMetadata, DataStore<?, ?> dataStore) {
        this.repositoryMetadata = repositoryMetadata;
        this.dataStore = dataStore;
    }

    @Override
    public RepositoryMetadata getRepositoryMetadata() {
        return repositoryMetadata;
    }

    @Override
    public DataStore<?, ?> getDataStore() {
        return dataStore;
    }
}
