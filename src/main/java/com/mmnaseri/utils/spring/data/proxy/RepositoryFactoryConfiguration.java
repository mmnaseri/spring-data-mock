package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/8/15)
 */
public interface RepositoryFactoryConfiguration {

    RepositoryMetadataResolver getRepositoryMetadataResolver();

    QueryDescriptionExtractor getDescriptionExtractor();

    DataFunctionRegistry getFunctionRegistry();

    DataStoreRegistry getDataStoreRegistry();

    ResultAdapterContext getResultAdapterContext();

    TypeMappingContext getTypeMappingContext();

    DataStoreEventListenerContext getEventListenerContext();

    NonDataOperationInvocationHandler getOperationInvocationHandler();

}
