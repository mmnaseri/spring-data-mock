package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.InvocationMapping;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class DataOperationInvocationHandler<K extends Serializable, E> implements InvocationHandler {

    private final RepositoryMetadata repositoryMetadata;
    private final DataStore<K, E> dataStore;
    private final ResultAdapterContext adapterContext;
    private final ResultConverter converter;
    private final List<InvocationMapping<K, E>> mappings;

    public DataOperationInvocationHandler(RepositoryMetadata repositoryMetadata, List<InvocationMapping<K, E>> mappings, DataStore<K, E> dataStore, ResultAdapterContext adapterContext) {
        this.repositoryMetadata = repositoryMetadata;
        this.mappings = mappings;
        this.dataStore = dataStore;
        this.adapterContext = adapterContext;
        this.converter = new DefaultResultConverter();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Invocation methodInvocation = new ImmutableInvocation(method, args);
        for (InvocationMapping<K, E> mapping : mappings) {
            if (mapping.getMethod().equals(method)) {
                final DataStoreOperation<?, K, E> operation = mapping.getOperation();
                final Object operationResult = operation.execute(dataStore, repositoryMetadata, methodInvocation);
                final Object convertedResult = converter.convert(methodInvocation, operationResult);
                return adapterContext.adapt(methodInvocation, convertedResult);
            }
        }
        throw new IllegalStateException("No operation mapping found for method " + method);
    }

}
