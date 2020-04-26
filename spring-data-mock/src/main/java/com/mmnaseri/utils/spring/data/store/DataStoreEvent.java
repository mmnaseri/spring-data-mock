package com.mmnaseri.utils.spring.data.store;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

/**
 * This interface indicates that a data store operation was requested or completed.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public interface DataStoreEvent {

    /**
     * @return the repository metadata associated with the given data store
     */
    RepositoryMetadata getRepositoryMetadata();

    /**
     * @return the data store that triggered this event
     */
    DataStore<?, ?> getDataStore();

}
