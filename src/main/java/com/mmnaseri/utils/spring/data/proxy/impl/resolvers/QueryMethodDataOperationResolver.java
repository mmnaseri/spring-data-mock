package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.DescribedDataStoreOperation;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.SelectDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.DataOperationResolver;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.QueryAnnotation;

import java.lang.reflect.Method;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
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
        if (AnnotationUtils.findAnnotation(method, QueryAnnotation.class) != null) {
            //we don't know how to handle vendor-specific query methods
            return null;
        }
        final QueryDescriptor descriptor = descriptionExtractor.extract(repositoryMetadata, method, configuration);
        return new DescribedDataStoreOperation<>(new SelectDataStoreOperation<>(descriptor), functionRegistry);
    }

}
