package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.impl.SelectDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;

import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:38 PM)
 */
public class SpyingSelectDataStoreOperation<K, E> extends SelectDataStoreOperation<K, E> {

    private boolean called = false;
    private final List<E> list;

    public SpyingSelectDataStoreOperation(QueryDescriptor descriptor, List<E> list) {
        super(descriptor);
        this.list = list;
    }

    @Override
    public List<E> execute(DataStore<K, E> store, RepositoryConfiguration configuration, Invocation invocation) {
        called = true;
        return list;
    }

    public boolean isCalled() {
        return called;
    }

}
