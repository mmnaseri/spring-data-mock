package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class MethodInvocationDataStoreOperation<K extends Serializable, E> implements DataStoreOperation<Object, K, E> {

    private final Object instance;
    private final Method method;

    public MethodInvocationDataStoreOperation(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
        method.setAccessible(true);
    }

    @Override
    public Object execute(DataStore<K, E> store, RepositoryMetadata repositoryMetadata, Invocation invocation) {
        final Object result;
        try {
            result = method.invoke(instance, invocation.getArguments());
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Failed to access target method: " + method, e);
        } catch (InvocationTargetException e) {
            throw new IllegalStateException("Method call resulted in internal error: " + method, e);
        }
        return result;
    }

}
