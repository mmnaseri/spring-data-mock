package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.InvocationMapping;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;
import com.mmnaseri.utils.spring.data.proxy.impl.converters.DefaultResultConverter;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.CopyOnWriteArraySet;

/**
 * <p>This class is in charge of handling a data operation that was triggered by invoking a repository method.</p>
 *
 * <p>The invocation is first considered by trying to find a data operation handler. If such a handler cannot be
 * found, we will try to handle it by finding the appropriate {@link com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler non-data
 * operation handler}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/23/15)
 */
@SuppressWarnings("WeakerAccess")
public class DataOperationInvocationHandler<K, E> implements InvocationHandler {

    private static final Log log = LogFactory.getLog(DataOperationInvocationHandler.class);
    private final DataStore<K, E> dataStore;
    private final ResultAdapterContext adapterContext;
    private final ResultConverter converter;
    private final RepositoryConfiguration repositoryConfiguration;
    private final List<InvocationMapping<K, E>> mappings;
    private final Map<Method, InvocationMapping<K, E>> cache = new ConcurrentHashMap<>();
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
        log.info("A method call to " + method + " has been intercepted. We will now try to find an appropriate invocation.");
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
            log.info("The invocation cannot be resolved using a data operation. We will try to handle this as a non-data operation");
            misses.add(method);
            return operationInvocationHandler.invoke(proxy, method, args);
        }
        final DataStoreOperation<?, K, E> operation = targetMapping.getOperation();
        log.info("Executing the operation for method " + method);
        final Object operationResult = operation.execute(dataStore, repositoryConfiguration, methodInvocation);
        log.info("Trying to see if any conversion is necessary on the object");
        final Object convertedResult = converter.convert(methodInvocation, operationResult);
        return adapterContext.adapt(methodInvocation, convertedResult);
    }

}
