package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/27/15)
 */
public class DefaultRepositoryFactoryConfiguration implements RepositoryFactoryConfiguration {

    private RepositoryMetadataResolver repositoryMetadataResolver;
    private QueryDescriptionExtractor descriptionExtractor;
    private DataFunctionRegistry functionRegistry;
    private DataStoreRegistry dataStoreRegistry;
    private ResultAdapterContext resultAdapterContext;
    private TypeMappingContext typeMappingContext;
    private DataStoreEventListenerContext eventListenerContext;
    private NonDataOperationInvocationHandler operationInvocationHandler;

    @Override
    public RepositoryMetadataResolver getRepositoryMetadataResolver() {
        return repositoryMetadataResolver;
    }

    public void setRepositoryMetadataResolver(RepositoryMetadataResolver repositoryMetadataResolver) {
        this.repositoryMetadataResolver = repositoryMetadataResolver;
    }

    @Override
    public QueryDescriptionExtractor getDescriptionExtractor() {
        return descriptionExtractor;
    }

    public void setDescriptionExtractor(QueryDescriptionExtractor descriptionExtractor) {
        this.descriptionExtractor = descriptionExtractor;
    }

    @Override
    public DataFunctionRegistry getFunctionRegistry() {
        return functionRegistry;
    }

    public void setFunctionRegistry(DataFunctionRegistry functionRegistry) {
        this.functionRegistry = functionRegistry;
    }

    @Override
    public DataStoreRegistry getDataStoreRegistry() {
        return dataStoreRegistry;
    }

    public void setDataStoreRegistry(DataStoreRegistry dataStoreRegistry) {
        this.dataStoreRegistry = dataStoreRegistry;
    }

    @Override
    public ResultAdapterContext getResultAdapterContext() {
        return resultAdapterContext;
    }

    public void setResultAdapterContext(ResultAdapterContext resultAdapterContext) {
        this.resultAdapterContext = resultAdapterContext;
    }

    @Override
    public TypeMappingContext getTypeMappingContext() {
        return typeMappingContext;
    }

    public void setTypeMappingContext(TypeMappingContext typeMappingContext) {
        this.typeMappingContext = typeMappingContext;
    }

    @Override
    public DataStoreEventListenerContext getEventListenerContext() {
        return eventListenerContext;
    }

    public void setEventListenerContext(DataStoreEventListenerContext eventListenerContext) {
        this.eventListenerContext = eventListenerContext;
    }

    @Override
    public NonDataOperationInvocationHandler getOperationInvocationHandler() {
        return operationInvocationHandler;
    }

    public void setOperationInvocationHandler(NonDataOperationInvocationHandler operationInvocationHandler) {
        this.operationInvocationHandler = operationInvocationHandler;
    }

}
