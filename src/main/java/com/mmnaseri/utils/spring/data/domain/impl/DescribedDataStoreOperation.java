package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.util.List;

/**
 * This is a data store operation that has a description attached to it. This means that it is a select operation that is
 * capable of taking care of applying a function to a given selection of objects.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
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
    public Object execute(DataStore<K, E> store, RepositoryConfiguration configuration, Invocation invocation) {
        final List<E> selection = selectOperation.execute(store, configuration, invocation);
        final QueryDescriptor descriptor = selectOperation.getDescriptor();
        if (descriptor.getFunction() == null) {
            return selection;
        }
        final DataFunction<?> function = functionRegistry.getFunction(descriptor.getFunction());
        return function.apply(store, descriptor, configuration, selection);
    }

    @Override
    public String toString() {
        return selectOperation.toString();
    }

}
