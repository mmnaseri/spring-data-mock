package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultResultConverter;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class DataOperationMethodInterceptor<K extends Serializable, E> implements MethodInterceptor {

    private final RepositoryMetadata repositoryMetadata;
    private final DataStoreOperation<?, K, E> operation;
    private final DataStore<K, E> dataStore;
    private final ResultAdapterContext adapterContext;
    private final ResultConverter converter;

    public DataOperationMethodInterceptor(RepositoryMetadata repositoryMetadata, DataStoreOperation<?, K, E> operation, DataStore<K, E> dataStore, ResultAdapterContext adapterContext) {
        this.repositoryMetadata = repositoryMetadata;
        this.operation = operation;
        this.dataStore = dataStore;
        this.adapterContext = adapterContext;
        this.converter = new DefaultResultConverter();
    }

    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        final Method method = invocation.getMethod();
        final ImmutableInvocation methodInvocation = new ImmutableInvocation(method, invocation.getArguments());
        final Object operationResult = operation.execute(dataStore, repositoryMetadata, methodInvocation);
        final Object convertedResult = converter.convert(methodInvocation, operationResult);
        return adapterContext.adapt(methodInvocation, convertedResult);
    }

}
