package com.mmnaseri.utils.spring.data.sample.models;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 5:24 PM)
 */
public class DummyEvent implements DataStoreEvent {

    @Override
    public RepositoryMetadata getRepositoryMetadata() {
        return null;
    }

    @Override
    public DataStore<?, ?> getDataStore() {
        return null;
    }

}
