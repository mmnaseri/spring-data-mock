package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.store.DataStore;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/6/15)
 */
public class AfterInsertDataStoreEvent extends AbstractEntityDataStoreEvent {

    public AfterInsertDataStoreEvent(RepositoryMetadata repositoryMetadata, DataStore<?, ?> dataStore, Object entity) {
        super(repositoryMetadata, dataStore, entity);
    }

}
