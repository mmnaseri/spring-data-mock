package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.DataOperationExecutionException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * This is a data store operation that delivers the operation by calling to a delegate method. This means that the results
 * of the operation are the same as what was returned by the method itself.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public class MethodInvocationDataStoreOperation<K extends Serializable, E> implements DataStoreOperation<Object, K, E> {

    private static final Log log = LogFactory.getLog(MethodInvocationDataStoreOperation.class);
    private final Object instance;
    private final Method method;

    public MethodInvocationDataStoreOperation(Object instance, Method method) {
        this.instance = instance;
        this.method = method;
    }

    @Override
    public Object execute(DataStore<K, E> store, RepositoryConfiguration configuration, Invocation invocation) {
        final Object result;
        try {
            log.info("Invoking method " + method + " to handle invocation " + invocation);
            result = method.invoke(instance, invocation.getArguments());
        } catch (IllegalAccessException e) {
            throw new DataOperationExecutionException("Failed to access target method: " + method, e);
        } catch (InvocationTargetException e) {
            throw new DataOperationExecutionException("Method call resulted in internal error: " + method, e.getTargetException());
        }
        return result;
    }

    public Object getInstance() {
        return instance;
    }

    public Method getMethod() {
        return method;
    }

    @Override
    public String toString() {
        return method + " on " + instance;
    }
}
