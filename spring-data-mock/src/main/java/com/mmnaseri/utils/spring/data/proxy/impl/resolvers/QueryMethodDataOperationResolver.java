package com.mmnaseri.utils.spring.data.proxy.impl.resolvers;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.DescribedDataStoreOperation;
import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.SelectDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.DataOperationResolver;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.QueryAnnotation;

import java.lang.reflect.Method;

/**
 * <p>This class will resolve methods to their query method equivalent by parsing their names and parameters.</p>
 *
 * <p>Even though, technically speaking, a class annotated with {@link QueryAnnotation} <em>is</em> a
 * query method, this class will ignore such methods since it doesn't know how to respond to native
 * queries.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("WeakerAccess")
public class QueryMethodDataOperationResolver implements DataOperationResolver {

    private static final Log log = LogFactory.getLog(QueryMethodDataOperationResolver.class);
    private final MethodQueryDescriptionExtractor descriptionExtractor;
    private final RepositoryMetadata repositoryMetadata;
    private final DataFunctionRegistry functionRegistry;
    private final RepositoryFactoryConfiguration configuration;

    public QueryMethodDataOperationResolver(MethodQueryDescriptionExtractor descriptionExtractor, RepositoryMetadata repositoryMetadata, DataFunctionRegistry functionRegistry, RepositoryFactoryConfiguration configuration) {
        this.descriptionExtractor = descriptionExtractor;
        this.repositoryMetadata = repositoryMetadata;
        this.functionRegistry = functionRegistry;
        this.configuration = configuration;
    }

    @Override
    public DataStoreOperation<?, ?, ?> resolve(Method method) {
        if (AnnotationUtils.findAnnotation(method, QueryAnnotation.class) != null) {
            log.info("Found a @Query annotation on the method " + method);
            //we don't know how to handle vendor-specific query methods
            return null;
        }
        log.info("Extracting query description from the method by parsing the method");
        final QueryDescriptor descriptor = descriptionExtractor.extract(repositoryMetadata, configuration, method);
        return new DescribedDataStoreOperation<>(new SelectDataStoreOperation<>(descriptor), functionRegistry);
    }

}
