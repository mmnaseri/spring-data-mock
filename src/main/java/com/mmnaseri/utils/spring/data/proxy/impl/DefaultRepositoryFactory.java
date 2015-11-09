package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.*;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.EventPublishingDataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;

import java.io.Serializable;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
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
    private final TypeMappingContext typeMappingContext;
    private final RepositoryFactoryConfiguration configuration;

    public DefaultRepositoryFactory(RepositoryFactoryConfiguration configuration) {
        this.configuration = configuration;
        this.repositoryMetadataResolver = configuration.getRepositoryMetadataResolver();
        this.descriptionExtractor = configuration.getDescriptionExtractor();
        this.functionRegistry = configuration.getFunctionRegistry();
        this.dataStoreRegistry = configuration.getDataStoreRegistry();
        this.adapterContext = configuration.getResultAdapterContext();
        this.typeMappingContext = configuration.getTypeMappingContext();
    }

    @Override
    public <E> E getInstance(KeyGenerator<? extends Serializable> keyGenerator, Class<E> repositoryInterface, Class... implementations) {
        final RepositoryMetadata metadata = getRepositoryMetadata(repositoryInterface);
        final DataStore<Serializable, Object> dataStore = getDataStore(metadata);
        final List<TypeMapping<?>> typeMappings = getTypeMappings(metadata, dataStore, keyGenerator, implementations);
        final DataOperationResolver operationResolver = new DefaultDataOperationResolver(typeMappings, descriptionExtractor, metadata, functionRegistry, configuration);
        final Method[] methods = repositoryInterface.getMethods();
        final List<InvocationMapping<? extends Serializable, ?>> invocationMappings = getInvocationMappings(operationResolver, methods);
        final List<Class<?>> boundImplementations = new LinkedList<Class<?>>();
        for (TypeMapping<?> mapping : typeMappings) {
            boundImplementations.add(mapping.getType());
        }
        final RepositoryConfiguration repositoryConfiguration = new ImmutableRepositoryConfiguration(metadata, keyGenerator, boundImplementations);
        //noinspection unchecked
        final InvocationHandler interceptor = new DataOperationInvocationHandler(repositoryConfiguration, invocationMappings, dataStore, adapterContext);
        final Object instance = Proxy.newProxyInstance(getClass().getClassLoader(), new Class[]{repositoryInterface}, interceptor);
        for (TypeMapping<?> typeMapping : typeMappings) {
            if (typeMapping.getInstance() instanceof RepositoryAware<?>) {
                //noinspection unchecked
                ((RepositoryAware) typeMapping.getInstance()).setRepository(instance);
            }
        }
        return repositoryInterface.cast(instance);
    }

    @Override
    public RepositoryFactoryConfiguration getConfiguration() {
        return configuration;
    }

    private List<TypeMapping<?>> getTypeMappings(RepositoryMetadata metadata, DataStore<Serializable, Object> dataStore, KeyGenerator<? extends Serializable> keyGenerator, Class[] implementations) {
        final List<TypeMapping<?>> typeMappings = new LinkedList<TypeMapping<?>>();
        final TypeMappingContext localContext = new DefaultTypeMappingContext(typeMappingContext);
        for (Class implementation : implementations) {
            localContext.register(metadata.getRepositoryInterface(), implementation);
        }
        typeMappings.addAll(localContext.getMappings(metadata.getRepositoryInterface()));
        for (TypeMapping<?> mapping : typeMappings) {
            if (mapping.getInstance() instanceof DataStoreAware<?, ?>) {
                DataStoreAware instance = (DataStoreAware<?, ?>) mapping.getInstance();
                instance.setDataStore(dataStore);
            }
            if (mapping.getInstance() instanceof RepositoryMetadataAware) {
                RepositoryMetadataAware instance = (RepositoryMetadataAware) mapping.getInstance();
                instance.setRepositoryMetadata(metadata);
            }
            if (mapping.getInstance() instanceof KeyGeneratorAware) {
                KeyGeneratorAware instance = (KeyGeneratorAware) mapping.getInstance();
                //noinspection unchecked
                instance.setKeyGenerator(keyGenerator);
            }
            if (mapping.getInstance() instanceof RepositoryFactoryConfigurationAware) {
                RepositoryFactoryConfigurationAware instance = (RepositoryFactoryConfigurationAware) mapping.getInstance();
                instance.setRepositoryFactoryConfiguration(configuration);
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
        DataStore<Serializable, Object> dataStore;
        if (dataStoreRegistry.has(metadata.getEntityType())) {
            //noinspection unchecked
            dataStore = (DataStore<Serializable, Object>) dataStoreRegistry.getDataStore(metadata.getEntityType());
        } else {
            //noinspection unchecked
            dataStore = new MemoryDataStore<Serializable, Object>((Class<Object>) metadata.getEntityType());
        }
        if (!(dataStore instanceof EventPublishingDataStore)) {
            dataStore = new EventPublishingDataStore<Serializable, Object>(dataStore, metadata, new DefaultDataStoreEventListenerContext(configuration.getEventListenerContext()));
        }
        dataStoreRegistry.register(dataStore);
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
