package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.proxy.InvocationMapping;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class ImmutableInvocationMapping<K extends Serializable, E> implements InvocationMapping<K, E> {

    private final Method method;
    private final DataStoreOperation<?, K, E> operation;

    public ImmutableInvocationMapping(Method method, DataStoreOperation<?, K, E> operation) {
        this.method = method;
        this.operation = operation;
    }

    @Override
    public Method getMethod() {
        return method;
    }

    @Override
    public DataStoreOperation<?, K, E> getOperation() {
        return operation;
    }

}
