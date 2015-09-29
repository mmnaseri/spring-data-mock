package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataAware;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.*;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class DefaultRepositoryFactory implements RepositoryFactory {
    
    private final RepositoryMetadataResolver repositoryMetadataResolver;
    private final Map<Class<?>, RepositoryMetadata> metadataMap = new ConcurrentHashMap<Class<?>, RepositoryMetadata>();
    private final QueryDescriptionExtractor descriptionExtractor;
    private final DataFunctionRegistry functionRegistry;
    private final DataStoreRegistry dataStoreRegistry;
    private final ResultAdapterContext adapterContext;

    public DefaultRepositoryFactory(RepositoryMetadataResolver repositoryMetadataResolver, QueryDescriptionExtractor descriptionExtractor, DataFunctionRegistry functionRegistry, DataStoreRegistry dataStoreRegistry, ResultAdapterContext adapterContext) {
        this.repositoryMetadataResolver = repositoryMetadataResolver;
        this.descriptionExtractor = descriptionExtractor;
        this.functionRegistry = functionRegistry;
        this.dataStoreRegistry = dataStoreRegistry;
        this.adapterContext = adapterContext;
    }

    @Override
    public <E> E getInstance(Class<E> repositoryInterface, Class... implementations) {
        final RepositoryMetadata metadata = getRepositoryMetadata(repositoryInterface);
        final DataStore<Serializable, Object> dataStore = getDataStore(metadata);
        final List<TypeMapping<?>> typeMappings = getTypeMappings(metadata, dataStore, implementations);
        final DataOperationResolver operationResolver = new DefaultDataOperationResolver(typeMappings, descriptionExtractor, metadata, functionRegistry);
        final Method[] methods = repositoryInterface.getMethods();
        final List<InvocationMapping<? extends Serializable, ?>> invocationMappings = getInvocationMappings(operationResolver, methods);
        //noinspection unchecked
        final InvocationHandler interceptor = new DataOperationInvocationHandler(metadata, invocationMappings, dataStore, adapterContext);
        final Object instance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{repositoryInterface}, interceptor);
        return repositoryInterface.cast(instance);
    }

    private List<TypeMapping<?>> getTypeMappings(RepositoryMetadata metadata, DataStore<Serializable, Object> dataStore, Class[] implementations) {
        final List<TypeMapping<?>> typeMappings = new LinkedList<TypeMapping<?>>();
        for (Class implementation : implementations) {
            if (Modifier.isAbstract(implementation.getModifiers()) || Modifier.isInterface(implementation.getModifiers())) {
                throw new IllegalStateException("Cannot instantiate a non-concrete class");
            }
            final Object instance1;
            try {
                instance1 = implementation.newInstance();
            } catch (InstantiationException e) {
                throw new IllegalStateException("Failed to instantiate an object of type " + implementation, e);
            } catch (IllegalAccessException e) {
                throw new IllegalStateException("Failed to access the constructor for " + implementation, e);
            }
            //noinspection unchecked
            typeMappings.add(new ImmutableTypeMapping<Object>(implementation, instance1));
        }
        for (TypeMapping<?> mapping : typeMappings) {
            if (mapping.getInstance() instanceof DataStoreAware<?, ?>) {
                DataStoreAware instance = (DataStoreAware<?, ?>) mapping.getInstance();
                instance.setDataStore(dataStore);
            }
            if (mapping.getInstance() instanceof RepositoryMetadataAware) {
                RepositoryMetadataAware instance = (RepositoryMetadataAware) mapping.getInstance();
                instance.setRepositoryMetadata(metadata);
            }
        }
        return typeMappings;
    }

    private <E> RepositoryMetadata getRepositoryMetadata(Class<E> repositoryInterface) {
        final RepositoryMetadata metadata;
        if (metadataMap.containsKey(repositoryInterface)) {
            metadata = metadataMap.get(repositoryInterface);
        } else {
            metadata = repositoryMetadataResolver.resolve(repositoryInterface);
            metadataMap.put(repositoryInterface, metadata);
        }
        return metadata;
    }

    private DataStore<Serializable, Object> getDataStore(RepositoryMetadata metadata) {
        final DataStore<Serializable, Object> dataStore;
        if (dataStoreRegistry.has(metadata.getEntityType())) {
            //noinspection unchecked
            dataStore = (DataStore<Serializable, Object>) dataStoreRegistry.getDataStore(metadata.getEntityType());
        } else {
            //noinspection unchecked
            dataStore = new MemoryDataStore<Serializable, Object>((Class<Object>) metadata.getEntityType());
            dataStoreRegistry.register(dataStore);
        }
        return dataStore;
    }

    private List<InvocationMapping<? extends Serializable, ?>> getInvocationMappings(DataOperationResolver operationResolver, Method[] methods) {
        final List<InvocationMapping<? extends Serializable, ?>> invocationMappings = new LinkedList<InvocationMapping<? extends Serializable, ?>>();
        for (Method method : methods) {
            final DataStoreOperation<?, ?, ?> operation = operationResolver.resolve(method);
            //noinspection unchecked
            invocationMappings.add(new ImmutableInvocationMapping<Serializable, Object>(method, (DataStoreOperation<?, Serializable, Object>) operation));
        }
        return invocationMappings;
    }

}