package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.store.DataStore;

/**
 * This class is the base class for any data store event that indicates a particular entity was involved in the evnet.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractEntityDataStoreEvent extends AbstractDataStoreEvent {

    private final Object entity;

    public AbstractEntityDataStoreEvent(RepositoryMetadata repositoryMetadata, DataStore<?, ?> dataStore,
                                        Object entity) {
        super(repositoryMetadata, dataStore);
        this.entity = entity;
    }

    public Object getEntity() {
        return entity;
    }

}
