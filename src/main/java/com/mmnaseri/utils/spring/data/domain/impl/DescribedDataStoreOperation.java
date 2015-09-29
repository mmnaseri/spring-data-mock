package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class DescribedDataStoreOperation<K extends Serializable, E> implements DataStoreOperation<Object, K, E> {
    
    private final SelectDataStoreOperation<K, E> selectOperation;
    private final DataFunctionRegistry functionRegistry;

    public DescribedDataStoreOperation(SelectDataStoreOperation<K, E> selectOperation, DataFunctionRegistry functionRegistry) {
        this.selectOperation = selectOperation;
        this.functionRegistry = functionRegistry;
    }

    @Override
    public Object execute(DataStore<K, E> store, RepositoryMetadata repositoryMetadata, Invocation invocation) {
        final List<E> selection = selectOperation.execute(store, repositoryMetadata, invocation);
        final QueryDescriptor descriptor = selectOperation.getDescriptor();
        if (descriptor.getFunction() == null) {
            return selection;
        }
        final DataFunction<?> function = functionRegistry.getFunction(descriptor.getFunction());
        return function.apply(store, descriptor, selection);
    }
    
}
