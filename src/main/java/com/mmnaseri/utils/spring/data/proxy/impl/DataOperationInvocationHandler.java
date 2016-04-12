package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.InvocationMapping;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class DataOperationInvocationHandler<K extends Serializable, E> implements InvocationHandler {

    private final DataStore<K, E> dataStore;
    private final ResultAdapterContext adapterContext;
    private final ResultConverter converter;
    private final RepositoryConfiguration repositoryConfiguration;
    private final List<InvocationMapping<K, E>> mappings;
    private final Map<Method, InvocationMapping<K, E>> cache = new ConcurrentHashMap<Method, InvocationMapping<K, E>>();
    private final Set<Method> misses = new CopyOnWriteArraySet<>();
    private final NonDataOperationInvocationHandler operationInvocationHandler;

    public DataOperationInvocationHandler(RepositoryConfiguration repositoryConfiguration, List<InvocationMapping<K, E>> mappings,
                                          DataStore<K, E> dataStore, ResultAdapterContext adapterContext,
                                          NonDataOperationInvocationHandler operationInvocationHandler) {
        this.repositoryConfiguration = repositoryConfiguration;
        this.mappings = mappings;
        this.dataStore = dataStore;
        this.adapterContext = adapterContext;
        this.operationInvocationHandler = operationInvocationHandler;
        this.converter = new DefaultResultConverter();
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        final Invocation methodInvocation = new ImmutableInvocation(method, args);
        InvocationMapping<K, E> targetMapping = null;
        if (!misses.contains(method)) {
            if (cache.containsKey(method)) {
                targetMapping = cache.get(method);
            } else {
                for (InvocationMapping<K, E> mapping : mappings) {
                    if (mapping.getMethod().equals(method)) {
                        targetMapping = mapping;
                        cache.put(method, targetMapping);
                        break;
                    }
                }
            }
        }
        if (targetMapping == null) {
            misses.add(method);
            return operationInvocationHandler.invoke(proxy, method, args);
        }
        final DataStoreOperation<?, K, E> operation = targetMapping.getOperation();
        final Object operationResult = operation.execute(dataStore, repositoryConfiguration, methodInvocation);
        final Object convertedResult = converter.convert(methodInvocation, operationResult);
        return adapterContext.adapt(methodInvocation, convertedResult);
    }

}
