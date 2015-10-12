package com.mmnaseri.utils.spring.data.proxy.dsl.config;

import com.mmnaseri.utils.spring.data.domain.OperatorContext;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultTypeMappingContext;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public class RepositoryFactoryConfigurationBuilder implements EventListenerContextConfigurer {

    private final DataFunctionRegistry functionRegistry;
    private final DataStoreRegistry dataStoreRegistry;
    private final ResultAdapterContext adapterContext;
    private final TypeMappingContext typeMappingContext;
    private final QueryDescriptionExtractor descriptionExtractor;
    private final RepositoryMetadataResolver repositoryMetadataResolver;
    private final DataStoreEventListenerContext listenerContext;

    public static MetadataResolverConfigurer givenOperators(OperatorContext context) {
        return new RepositoryFactoryConfigurationBuilder().withOperators(context);
    }

    public static MetadataResolverConfigurer givenDescriptionExtractor(QueryDescriptionExtractor extractor) {
        return new RepositoryFactoryConfigurationBuilder().extractQueryDescriptionUsing(extractor);
    }

    public static FunctionRegistryConfigurer resolveMetadataUsing(RepositoryMetadataResolver resolver) {
        return new RepositoryFactoryConfigurationBuilder().resolvingMetadataUsing(resolver);
    }

    public static DataStoreConfigurer givenFunctions(DataFunctionRegistry functionRegistry) {
        return new RepositoryFactoryConfigurationBuilder().withFunctions(functionRegistry);
    }

    public static ResultAdapterConfigurer givenDataSources(DataStoreRegistry dataStoreRegistry) {
        return new RepositoryFactoryConfigurationBuilder().withDataStores(dataStoreRegistry);
    }

    public static TypeMappingConfigurer givenResultAdapters(ResultAdapterContext adapterContext) {
        return new RepositoryFactoryConfigurationBuilder().withAdapters(adapterContext);
    }

    public static BuildFinalizer givenTypeMappings(TypeMappingContext typeMappingContext) {
        return new RepositoryFactoryConfigurationBuilder().withMappings(typeMappingContext);
    }

    public static QueryExtractionConfigurer givenEventListeners(DataStoreEventListenerContext listenerContext) {
        return new RepositoryFactoryConfigurationBuilder().withEventListeners(listenerContext);
    }

    public static RepositoryFactoryConfiguration defaultConfiguration() {
        return new RepositoryFactoryConfigurationBuilder().configure();
    }

    private RepositoryFactoryConfigurationBuilder() {
        this(new DefaultDataFunctionRegistry(), new DefaultDataStoreRegistry(), new DefaultResultAdapterContext(), new DefaultTypeMappingContext(), new QueryDescriptionExtractor(new DefaultOperatorContext()), new DefaultRepositoryMetadataResolver(), new DefaultDataStoreEventListenerContext());
    }

    private RepositoryFactoryConfigurationBuilder(DataFunctionRegistry functionRegistry, DataStoreRegistry dataStoreRegistry, ResultAdapterContext adapterContext, TypeMappingContext typeMappingContext, QueryDescriptionExtractor descriptionExtractor, RepositoryMetadataResolver repositoryMetadataResolver, DataStoreEventListenerContext listenerContext) {
        this.functionRegistry = functionRegistry;
        this.dataStoreRegistry = dataStoreRegistry;
        this.adapterContext = adapterContext;
        this.typeMappingContext = typeMappingContext;
        this.descriptionExtractor = descriptionExtractor;
        this.repositoryMetadataResolver = repositoryMetadataResolver;
        this.listenerContext = listenerContext;
    }


    @Override
    public DataStoreConfigurer withFunctions(DataFunctionRegistry functionRegistry) {
        return new RepositoryFactoryConfigurationBuilder(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, descriptionExtractor, repositoryMetadataResolver, listenerContext);
    }

    @Override
    public ResultAdapterConfigurer withDataStores(DataStoreRegistry dataStoreRegistry) {
        return new RepositoryFactoryConfigurationBuilder(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, descriptionExtractor, repositoryMetadataResolver, listenerContext);
    }

    @Override
    public TypeMappingConfigurer withAdapters(ResultAdapterContext adapterContext) {
        return new RepositoryFactoryConfigurationBuilder(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, descriptionExtractor, repositoryMetadataResolver, listenerContext);
    }

    @Override
    public BuildFinalizer withMappings(TypeMappingContext typeMappingContext) {
        return new RepositoryFactoryConfigurationBuilder(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, descriptionExtractor, repositoryMetadataResolver, listenerContext);
    }

    @Override
    public MetadataResolverConfigurer extractQueryDescriptionUsing(QueryDescriptionExtractor descriptionExtractor) {
        return new RepositoryFactoryConfigurationBuilder(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, descriptionExtractor, repositoryMetadataResolver, listenerContext);
    }

    @Override
    public FunctionRegistryConfigurer resolvingMetadataUsing(RepositoryMetadataResolver repositoryMetadataResolver) {
        return new RepositoryFactoryConfigurationBuilder(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, descriptionExtractor, repositoryMetadataResolver, listenerContext);
    }

    @Override
    public MetadataResolverConfigurer withOperators(OperatorContext operatorContext) {
        return new RepositoryFactoryConfigurationBuilder(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, new QueryDescriptionExtractor(operatorContext), repositoryMetadataResolver, listenerContext);
    }

    @Override
    public QueryExtractionConfigurer withEventListeners(DataStoreEventListenerContext listenerContext) {
        return new RepositoryFactoryConfigurationBuilder(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, descriptionExtractor, repositoryMetadataResolver, listenerContext);
    }

    @Override
    public RepositoryFactoryConfiguration configure() {
        return new ImmutableRepositoryFactoryConfiguration(functionRegistry, dataStoreRegistry, adapterContext, typeMappingContext, descriptionExtractor, repositoryMetadataResolver, listenerContext);
    }

    private static class ImmutableRepositoryFactoryConfiguration implements RepositoryFactoryConfiguration {

        private final DataFunctionRegistry functionRegistry;
        private final DataStoreRegistry dataStoreRegistry;
        private final ResultAdapterContext resultAdapterContext;
        private final TypeMappingContext typeMappingContext;
        private final QueryDescriptionExtractor descriptionExtractor;
        private final RepositoryMetadataResolver repositoryMetadataResolver;
        private final DataStoreEventListenerContext listenerContext;

        private ImmutableRepositoryFactoryConfiguration(DataFunctionRegistry functionRegistry, DataStoreRegistry dataStoreRegistry, ResultAdapterContext resultAdapterContext, TypeMappingContext typeMappingContext, QueryDescriptionExtractor descriptionExtractor, RepositoryMetadataResolver repositoryMetadataResolver, DataStoreEventListenerContext listenerContext) {
            this.functionRegistry = functionRegistry;
            this.dataStoreRegistry = dataStoreRegistry;
            this.resultAdapterContext = resultAdapterContext;
            this.typeMappingContext = typeMappingContext;
            this.descriptionExtractor = descriptionExtractor;
            this.repositoryMetadataResolver = repositoryMetadataResolver;
            this.listenerContext = listenerContext;
        }
        
        @Override
        public DataFunctionRegistry getFunctionRegistry() {
            return functionRegistry;
        }

        @Override
        public DataStoreRegistry getDataStoreRegistry() {
            return dataStoreRegistry;
        }

        @Override
        public ResultAdapterContext getResultAdapterContext() {
            return resultAdapterContext;
        }

        @Override
        public TypeMappingContext getTypeMappingContext() {
            return typeMappingContext;
        }

        @Override
        public DataStoreEventListenerContext getEventListenerContext() {
            return listenerContext;
        }

        @Override
        public QueryDescriptionExtractor getDescriptionExtractor() {
            return descriptionExtractor;
        }

        @Override
        public RepositoryMetadataResolver getRepositoryMetadataResolver() {
            return repositoryMetadataResolver;
        }

    }

}
