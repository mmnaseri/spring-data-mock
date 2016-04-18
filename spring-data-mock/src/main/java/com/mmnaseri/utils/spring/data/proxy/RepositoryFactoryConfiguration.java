package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;

/**
 * This class will be used to hold all the pieces of information required to instantiate a repository.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/8/15)
 */
public interface RepositoryFactoryConfiguration {

    /**
     * @return the repository metadata resolver
     */
    RepositoryMetadataResolver getRepositoryMetadataResolver();

    /**
     * @return the description extractor used to extract query metadata from a query method
     */
    QueryDescriptionExtractor getDescriptionExtractor();

    /**
     * @return the function registry containing all the functions used when executing the queries
     */
    DataFunctionRegistry getFunctionRegistry();

    /**
     * @return the data store registry
     */
    DataStoreRegistry getDataStoreRegistry();

    /**
     * @return the result adapter context
     */
    ResultAdapterContext getResultAdapterContext();

    /**
     * @return the type mapping context
     */
    TypeMappingContext getTypeMappingContext();

    /**
     * @return the data store event listener context
     */
    DataStoreEventListenerContext getEventListenerContext();

    /**
     * @return the non-data operation invocation handler
     */
    NonDataOperationInvocationHandler getOperationInvocationHandler();

}
