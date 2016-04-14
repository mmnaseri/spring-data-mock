package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.store.DataStore;

/**
 * This event indicates that an entity is scheduled to be deleted.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/6/15)
 */
@SuppressWarnings("WeakerAccess")
public class BeforeDeleteDataStoreEvent extends AbstractEntityDataStoreEvent {

    public BeforeDeleteDataStoreEvent(RepositoryMetadata repositoryMetadata, DataStore<?, ?> dataStore, Object entity) {
        super(repositoryMetadata, dataStore, entity);
    }

}
