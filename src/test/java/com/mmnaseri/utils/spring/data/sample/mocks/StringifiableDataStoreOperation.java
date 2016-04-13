package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 6:50 PM)
 */
public class StringifiableDataStoreOperation<R, K extends Serializable, E> implements DataStoreOperation<R, K, E> {

    private final String string;

    public StringifiableDataStoreOperation(String string) {
        this.string = string;
    }

    @Override
    public R execute(DataStore store, RepositoryConfiguration configuration, Invocation invocation) {
        return null;
    }

    @Override
    public String toString() {
        return string;
    }

}
