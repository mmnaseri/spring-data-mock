package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.OperatorContext;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.dsl.mock.Implementation;
import com.mmnaseri.utils.spring.data.dsl.mock.ImplementationAnd;
import com.mmnaseri.utils.spring.data.dsl.mock.RepositoryMockBuilder;
import com.mmnaseri.utils.spring.data.proxy.*;
import com.mmnaseri.utils.spring.data.proxy.impl.*;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.*;
import com.mmnaseri.utils.spring.data.store.impl.AuditDataEventListener;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import org.springframework.data.domain.AuditorAware;

import javax.annotation.Nonnull;
import java.util.Optional;

/**
 * This class implements the DSL used to configure and build a repository factory object.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public class RepositoryFactoryBuilder
        implements Start, DataFunctionsAnd, DataStoresAnd, EventListenerAnd, MappingContextAnd, OperatorsAnd,
                   ResultAdaptersAnd, OperationHandlersAnd {

    public static final String DEFAULT_USER = "User";
    private RepositoryMetadataResolver metadataResolver;
    private MethodQueryDescriptionExtractor queryDescriptionExtractor;
    private DataFunctionRegistry functionRegistry;
    private DataStoreRegistry dataStoreRegistry;
    private ResultAdapterContext resultAdapterContext;
    private TypeMappingContext typeMappingContext;
    private DataStoreEventListenerContext eventListenerContext;
    private NonDataOperationInvocationHandler operationInvocationHandler;
    private KeyGenerator<?> defaultKeyGenerator;

    /**
     * @return the default configuration
     */
    public static RepositoryFactoryConfiguration defaultConfiguration() {
        final RepositoryFactoryBuilder builder = (RepositoryFactoryBuilder) builder();
        return new ImmutableRepositoryFactoryConfiguration(
                builder.metadataResolver,
                builder.queryDescriptionExtractor,
                builder.functionRegistry,
                builder.dataStoreRegistry,
                builder.resultAdapterContext,
                builder.typeMappingContext,
                builder.eventListenerContext,
                builder.operationInvocationHandler,
                builder.defaultKeyGenerator);
    }

    /**
     * Starting point for writing code in the builder's DSL
     *
     * @return an instance of the builder
     */
    public static Start builder() {
        return new RepositoryFactoryBuilder();
    }

    /**
     * Start the configuration DSL by considering the provided configuration as the default fallback
     *
     * @param configuration the fallback configuration
     * @return an instance of the builder
     */
    public static Start given(RepositoryFactoryConfiguration configuration) {
        final RepositoryFactoryBuilder builder = new RepositoryFactoryBuilder();
        builder.metadataResolver = getOrDefault(configuration.getRepositoryMetadataResolver(),
                                                builder.metadataResolver);
        builder.queryDescriptionExtractor = getOrDefault(configuration.getDescriptionExtractor(),
                                                         builder.queryDescriptionExtractor);
        builder.functionRegistry = getOrDefault(configuration.getFunctionRegistry(), builder.functionRegistry);
        builder.dataStoreRegistry = getOrDefault(configuration.getDataStoreRegistry(), builder.dataStoreRegistry);
        builder.resultAdapterContext = getOrDefault(configuration.getResultAdapterContext(),
                                                    builder.resultAdapterContext);
        builder.typeMappingContext = getOrDefault(configuration.getTypeMappingContext(), builder.typeMappingContext);
        builder.eventListenerContext = getOrDefault(configuration.getEventListenerContext(),
                                                    builder.eventListenerContext);
        builder.operationInvocationHandler = getOrDefault(configuration.getOperationInvocationHandler(),
                                                          builder.operationInvocationHandler);
        builder.defaultKeyGenerator = getOrDefault(configuration.getDefaultKeyGenerator(), builder.defaultKeyGenerator);
        return builder;
    }

    private static <E> E getOrDefault(E value, E defaultValue) {
        return value != null ? value : defaultValue;
    }

    /**
     * @return the default factory
     */
    public static RepositoryFactory defaultFactory() {
        return new DefaultRepositoryFactory(defaultConfiguration());
    }

    private RepositoryFactoryBuilder() {
        metadataResolver = new DefaultRepositoryMetadataResolver();
        queryDescriptionExtractor = new MethodQueryDescriptionExtractor(new DefaultOperatorContext());
        functionRegistry = new DefaultDataFunctionRegistry();
        dataStoreRegistry = new DefaultDataStoreRegistry();
        resultAdapterContext = new DefaultResultAdapterContext();
        typeMappingContext = new DefaultTypeMappingContext();
        eventListenerContext = new DefaultDataStoreEventListenerContext();
        operationInvocationHandler = new NonDataOperationInvocationHandler();
        //by default, we do not want any key generator, unless one is specified
        defaultKeyGenerator = null;
    }

    @Override
    public QueryDescriptionConfigurer resolveMetadataUsing(RepositoryMetadataResolver metadataResolver) {
        this.metadataResolver = metadataResolver;
        return this;
    }

    @Override
    public DataFunctions withOperators(OperatorContext context) {
        queryDescriptionExtractor = new MethodQueryDescriptionExtractor(context);
        return this;
    }

    @Override
    public OperatorsAnd registerOperator(Operator operator) {
        queryDescriptionExtractor.getOperatorContext().register(operator);
        return this;
    }

    @Override
    public DataFunctions extractQueriesUsing(MethodQueryDescriptionExtractor extractor) {
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
    public <E, K> DataStoresAnd registerDataStore(DataStore<K, E> dataStore) {
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
    public FallbackKeyGenerator withOperationHandlers(NonDataOperationInvocationHandler invocationHandler) {
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
    public <E, K> DataStoresAnd and(DataStore<K, E> dataStore) {
        dataStoreRegistry.register(dataStore);
        return this;
    }

    @Override
    public OperationHandlersAnd and(NonDataOperationHandler handler) {
        operationInvocationHandler.register(handler);
        return this;
    }

    @Override
    public EventListener withDefaultKeyGenerator(KeyGenerator<?> keyGenerator) {
        defaultKeyGenerator = keyGenerator;
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
        return new DefaultRepositoryFactory(configure());
    }

    @Override
    public RepositoryFactoryConfiguration configure() {
        return new ImmutableRepositoryFactoryConfiguration(metadataResolver, queryDescriptionExtractor,
                                                           functionRegistry, dataStoreRegistry, resultAdapterContext,
                                                           typeMappingContext, eventListenerContext,
                                                           operationInvocationHandler, defaultKeyGenerator);
    }

    @Override
    public <S> Implementation generateKeysUsing(KeyGenerator<S> keyGenerator) {
        return new RepositoryMockBuilder().useFactory(build()).generateKeysUsing(keyGenerator);
    }

    @Override
    public <S, G extends KeyGenerator<S>> Implementation generateKeysUsing(Class<G> generatorType) {
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

    /**
     * An auditor aware that returns the static value of {@link #DEFAULT_USER}
     */
    @SuppressWarnings("WeakerAccess")
    public static class DefaultAuditorAware implements AuditorAware<String> {

        /**
         * @return {@link #DEFAULT_USER}
         */
        @Override
        @Nonnull
        public Optional<String> getCurrentAuditor() {
            return Optional.of(DEFAULT_USER);
        }

    }

}
