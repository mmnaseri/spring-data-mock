package com.mmnaseri.utils.spring.data.store;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/6/15)
 */
public interface DataStoreEvent {

    RepositoryMetadata getRepositoryMetadata();

    DataStore<?, ?> getDataStore();

}
