package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.OperatorContext;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.dsl.mock.Implementation;
import com.mmnaseri.utils.spring.data.dsl.mock.ImplementationAnd;
import com.mmnaseri.utils.spring.data.dsl.mock.RepositoryMockBuilder;
import com.mmnaseri.utils.spring.data.proxy.*;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultTypeMappingContext;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.*;
import com.mmnaseri.utils.spring.data.store.impl.AuditDataEventListener;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import org.springframework.data.domain.AuditorAware;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public class RepositoryFactoryBuilder implements Start, DataFunctionsAnd, DataStoresAnd, EventListenerAnd, MappingContextAnd, OperatorsAnd, ResultAdaptersAnd, OperationHandlersAnd {

    private static RepositoryFactory DEFAULT_FACTORY;
    private static final String DEFAULT_USER = "User";
    private RepositoryMetadataResolver metadataResolver;
    private QueryDescriptionExtractor queryDescriptionExtractor;
    private DataFunctionRegistry functionRegistry;
    private DataStoreRegistry dataStoreRegistry;
    private ResultAdapterContext resultAdapterContext;
    private TypeMappingContext typeMappingContext;
    private DataStoreEventListenerContext eventListenerContext;
    private NonDataOperationInvocationHandler operationInvocationHandler;

    public static Start builder() {
        return new RepositoryFactoryBuilder();
    }
    
    public static RepositoryFactoryConfiguration defaultConfiguration() {
        final RepositoryFactoryBuilder builder = (RepositoryFactoryBuilder) builder();
        return new ImmutableRepositoryFactoryConfiguration(builder.metadataResolver, builder.queryDescriptionExtractor, builder.functionRegistry, builder.dataStoreRegistry, builder.resultAdapterContext, builder.typeMappingContext, builder.eventListenerContext, builder.operationInvocationHandler);
    }
    
    public static RepositoryFactory defaultFactory() {
        if (DEFAULT_FACTORY == null) {
            DEFAULT_FACTORY = builder().build();
        }
        return DEFAULT_FACTORY;
    }

    private RepositoryFactoryBuilder() {
        metadataResolver = new DefaultRepositoryMetadataResolver();
        queryDescriptionExtractor = new QueryDescriptionExtractor(new DefaultOperatorContext());
        functionRegistry = new DefaultDataFunctionRegistry();
        dataStoreRegistry = new DefaultDataStoreRegistry();
        resultAdapterContext = new DefaultResultAdapterContext();
        typeMappingContext = new DefaultTypeMappingContext();
        eventListenerContext = new DefaultDataStoreEventListenerContext();
        operationInvocationHandler = new NonDataOperationInvocationHandler();
    }

    @Override
    public QueryDescriptionConfigurer resolveMetadataUsing(RepositoryMetadataResolver metadataResolver) {
        this.metadataResolver = metadataResolver;
        return this;
    }

    @Override
    public DataFunctions withOperators(OperatorContext context) {
        queryDescriptionExtractor = new QueryDescriptionExtractor(context);
        return this;
    }

    @Override
    public OperatorsAnd registerOperator(Operator operator) {
        queryDescriptionExtractor.getOperatorContext().register(operator);
        return this;
    }

    @Override
    public DataFunctions extractQueriesUsing(QueryDescriptionExtractor extractor) {
        queryDescriptionExtractor = extractor;
        return this;
    }

    @Override
    public DataStores withDataFunctions(DataFunctionRegistry registry) {
        functionRegistry = registry;
        return this;
    }

    @Override
    public <R> DataFunctionsAnd registerFunction(String name, DataFunction<R> function) {
        functionRegistry.register(name, function);
        return this;
    }

    @Override
    public ResultAdapters withDataStores(DataStoreRegistry registry) {
        dataStoreRegistry = registry;
        return this;
    }

    @Override
    public <E, K extends Serializable> DataStoresAnd registerDataStore(DataStore<K, E> dataStore) {
        dataStoreRegistry.register(dataStore);
        return this;
    }

    @Override
    public MappingContext withAdapters(ResultAdapterContext context) {
        resultAdapterContext = context;
        return this;
    }

    @Override
    public <E> ResultAdaptersAnd adaptResultsUsing(ResultAdapter<E> adapter) {
        resultAdapterContext.register(adapter);
        return this;
    }

    @Override
    public EventListener withMappings(TypeMappingContext context) {
        typeMappingContext = context;
        return this;
    }

    @Override
    public MappingContextAnd honoringImplementation(Class<?> superType, Class<?> implementation) {
        typeMappingContext.register(superType, implementation);
        return this;
    }


    @Override
    public EventListener withOperationHandlers(NonDataOperationInvocationHandler invocationHandler) {
        operationInvocationHandler = invocationHandler;
        return this;
    }

    @Override
    public OperationHandlersAnd withOperationHandler(NonDataOperationHandler handler) {
        operationInvocationHandler.register(handler);
        return this;
    }

    @Override
    public End withListeners(DataStoreEventListenerContext context) {
        eventListenerContext = context;
        return this;
    }

    @Override
    public <E extends DataStoreEvent> EventListenerAnd withListener(DataStoreEventListener<E> listener) {
        eventListenerContext.register(listener);
        return this;
    }

    @Override
    public EventListener enableAuditing(AuditorAware auditorAware) {
        return (EventListener) and(new AuditDataEventListener(auditorAware));
    }

    @Override
    public EventListener enableAuditing() {
        return enableAuditing(new DefaultAuditorAware());
    }

    @Override
    public <R> DataFunctionsAnd and(String name, DataFunction<R> function) {
        functionRegistry.register(name, function);
        return this;
    }

    @Override
    public <E, K extends Serializable> DataStoresAnd and(DataStore<K, E> dataStore) {
        dataStoreRegistry.register(dataStore);
        return this;
    }

    @Override
    public OperationHandlersAnd and(NonDataOperationHandler handler) {
        operationInvocationHandler.register(handler);
        return this;
    }

    @Override
    public <E extends DataStoreEvent> EventListenerAnd and(DataStoreEventListener<E> listener) {
        eventListenerContext.register(listener);
        return this;
    }

    @Override
    public MappingContextAnd and(Class<?> superType, Class<?> implementation) {
        typeMappingContext.register(superType, implementation);
        return this;
    }

    @Override
    public OperatorsAnd and(Operator operator) {
        queryDescriptionExtractor.getOperatorContext().register(operator);
        return this;
    }

    @Override
    public <E> ResultAdaptersAnd and(ResultAdapter<E> adapter) {
        resultAdapterContext.register(adapter);
        return this;
    }

    @Override
    public RepositoryFactory build() {
        return new DefaultRepositoryFactory(new ImmutableRepositoryFactoryConfiguration(metadataResolver, queryDescriptionExtractor, functionRegistry, dataStoreRegistry, resultAdapterContext, typeMappingContext, eventListenerContext, operationInvocationHandler));
    }

    @Override
    public <S extends Serializable> Implementation generateKeysUsing(KeyGenerator<S> keyGenerator) {
        return new RepositoryMockBuilder().useFactory(build()).generateKeysUsing(keyGenerator);
    }

    @Override
    public <S extends Serializable, G extends KeyGenerator<S>> Implementation generateKeysUsing(Class<G> generatorType) {
        return new RepositoryMockBuilder().useFactory(build()).generateKeysUsing(generatorType);
    }

    @Override
    public Implementation withoutGeneratingKeys() {
        return new RepositoryMockBuilder().useFactory(build()).withoutGeneratingKeys();
    }

    @Override
    public ImplementationAnd usingImplementation(Class<?> implementation) {
        return new RepositoryMockBuilder().useFactory(build()).usingImplementation(implementation);
    }

    @Override
    public <E> E mock(Class<E> repositoryInterface) {
        return new RepositoryMockBuilder().useFactory(build()).mock(repositoryInterface);
    }

    private static class ImmutableRepositoryFactoryConfiguration implements RepositoryFactoryConfiguration {

        private final RepositoryMetadataResolver metadataResolver;
        private final QueryDescriptionExtractor queryDescriptionExtractor;
        private final DataFunctionRegistry functionRegistry;
        private final DataStoreRegistry dataStoreRegistry;
        private final ResultAdapterContext resultAdapterContext;
        private final TypeMappingContext typeMappingContext;
        private final DataStoreEventListenerContext eventListenerContext;
        private final NonDataOperationInvocationHandler operationInvocationHandler;

        private ImmutableRepositoryFactoryConfiguration(RepositoryMetadataResolver metadataResolver, QueryDescriptionExtractor queryDescriptionExtractor, DataFunctionRegistry functionRegistry, DataStoreRegistry dataStoreRegistry, ResultAdapterContext resultAdapterContext, TypeMappingContext typeMappingContext, DataStoreEventListenerContext eventListenerContext, NonDataOperationInvocationHandler operationInvocationHandler) {
            this.metadataResolver = metadataResolver;
            this.queryDescriptionExtractor = queryDescriptionExtractor;
            this.functionRegistry = functionRegistry;
            this.dataStoreRegistry = dataStoreRegistry;
            this.resultAdapterContext = resultAdapterContext;
            this.typeMappingContext = typeMappingContext;
            this.eventListenerContext = eventListenerContext;
            this.operationInvocationHandler = operationInvocationHandler;
        }

        @Override
        public RepositoryMetadataResolver getRepositoryMetadataResolver() {
            return metadataResolver;
        }

        @Override
        public QueryDescriptionExtractor getDescriptionExtractor() {
            return queryDescriptionExtractor;
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
            return eventListenerContext;
        }

        @Override
        public NonDataOperationInvocationHandler getOperationInvocationHandler() {
            return operationInvocationHandler;
        }

    }

    private static class DefaultAuditorAware implements AuditorAware<String> {

        @Override
        public String getCurrentAuditor() {
            return DEFAULT_USER;
        }

    }

}
