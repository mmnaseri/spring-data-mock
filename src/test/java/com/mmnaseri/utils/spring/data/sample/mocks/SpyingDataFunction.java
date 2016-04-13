package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class SpyingDataFunction<R> implements DataFunction<R> {

    private final List<DataFunctionInvocation<?, ?>> invocations = new ArrayList<>();
    private final DataFunction<R> delegate;

    public SpyingDataFunction(DataFunction<R> delegate) {
        this.delegate = delegate;
    }

    @Override
    public <K extends Serializable, E> R apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration configuration, List<E> selection) {
        invocations.add(new DataFunctionInvocation<>(dataStore, query, configuration, selection));
        if (delegate != null) {
            return delegate.apply(dataStore, query, configuration, selection);
        }
        return null;
    }

    public List<DataFunctionInvocation<?, ?>> getInvocations() {
        return invocations;
    }

    public DataFunction<R> getDelegate() {
        return delegate;
    }

}
