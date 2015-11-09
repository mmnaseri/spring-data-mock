package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.DescribedDataStoreOperation;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.SelectDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.DataOperationResolver;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class QueryMethodDataOperationResolver implements DataOperationResolver {

    private final QueryDescriptionExtractor descriptionExtractor;
    private final RepositoryMetadata repositoryMetadata;
    private final DataFunctionRegistry functionRegistry;
    private final RepositoryFactoryConfiguration configuration;

    public QueryMethodDataOperationResolver(QueryDescriptionExtractor descriptionExtractor, RepositoryMetadata repositoryMetadata, DataFunctionRegistry functionRegistry, RepositoryFactoryConfiguration configuration) {
        this.descriptionExtractor = descriptionExtractor;
        this.repositoryMetadata = repositoryMetadata;
        this.functionRegistry = functionRegistry;
        this.configuration = configuration;
    }

    @Override
    public DataStoreOperation<?, ?, ?> resolve(Method method) {
        final QueryDescriptor descriptor = descriptionExtractor.extract(repositoryMetadata, method, configuration);
        return new DescribedDataStoreOperation<Serializable, Object>(new SelectDataStoreOperation<Serializable, Object>(descriptor), functionRegistry);
    }

}
