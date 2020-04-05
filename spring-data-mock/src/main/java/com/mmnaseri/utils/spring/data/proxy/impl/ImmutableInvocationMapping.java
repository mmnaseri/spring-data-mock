package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.proxy.InvocationMapping;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.lang.reflect.Method;

/**
 * <p>This class is an immutable invocation mapping.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("WeakerAccess")
public class ImmutableInvocationMapping<K, E> implements InvocationMapping<K, E> {

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

    @Override
    public String toString() {
        return method + " -> " + operation;
    }

}
