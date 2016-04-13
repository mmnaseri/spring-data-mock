package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.OperatorContext;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableOperator;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.dsl.mock.RepositoryMockBuilder;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultTypeMappingContext;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;
import com.mmnaseri.utils.spring.data.proxy.impl.adapters.VoidResultAdapter;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.sample.mocks.AllCatchingEventListener;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingDataFunction;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingHandler;
import com.mmnaseri.utils.spring.data.sample.repositories.*;
import com.mmnaseri.utils.spring.data.store.*;
import com.mmnaseri.utils.spring.data.store.impl.AuditDataEventListener;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.springframework.data.domain.AuditorAware;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 11:42 AM)
 */
public class RepositoryFactoryBuilderTest {

    @Test
    public void testDefaultConfiguration() throws Exception {
        final RepositoryFactoryConfiguration configuration = RepositoryFactoryBuilder.defaultConfiguration();
        assertThat(configuration, is(notNullValue()));
        assertThat(configuration.getDataStoreRegistry(), is(notNullValue()));
        assertThat(configuration.getTypeMappingContext(), is(notNullValue()));
        assertThat(configuration.getResultAdapterContext(), is(notNullValue()));
        assertThat(configuration.getRepositoryMetadataResolver(), is(notNullValue()));
        assertThat(configuration.getOperationInvocationHandler(), is(notNullValue()));
        assertThat(configuration.getFunctionRegistry(), is(notNullValue()));
        assertThat(configuration.getEventListenerContext(), is(notNullValue()));
        assertThat(configuration.getDescriptionExtractor(), is(notNullValue()));
        assertThat(RepositoryFactoryBuilder.defaultConfiguration(), is(RepositoryFactoryBuilder.defaultConfiguration()));
    }

    @Test
    public void testDefaultFactory() throws Exception {
        final RepositoryFactory factory = RepositoryFactoryBuilder.defaultFactory();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(RepositoryFactoryBuilder.defaultConfiguration()));
        assertThat(factory, is(RepositoryFactoryBuilder.defaultFactory()));
    }

    @Test
    public void testUsingCustomMetadataResolver() throws Exception {
        final DefaultRepositoryMetadataResolver resolver = new DefaultRepositoryMetadataResolver();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().resolveMetadataUsing(resolver).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getRepositoryMetadataResolver(), is(notNullValue()));
        assertThat(factory.getConfiguration().getRepositoryMetadataResolver(), Matchers.<RepositoryMetadataResolver>is(resolver));
    }

    @Test
    public void testUsingCustomQueryDescriptor() throws Exception {
        final QueryDescriptionExtractor queryDescriptionExtractor = new QueryDescriptionExtractor(new DefaultOperatorContext());
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().extractQueriesUsing(queryDescriptionExtractor).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor(), is(queryDescriptionExtractor));
    }

    @Test
    public void testUsingCustomOperatorContext() throws Exception {
        final DefaultOperatorContext operatorContext = new DefaultOperatorContext();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withOperators(operatorContext).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor().getOperatorContext(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor().getOperatorContext(), Matchers.<OperatorContext>is(operatorContext));
    }

    @Test
    public void testUsingDefaultOperatorContextWithAdditionalOperators() throws Exception {
        final ImmutableOperator x = new ImmutableOperator("x", 0, null, "X");
        final ImmutableOperator y = new ImmutableOperator("y", 0, null, "Y");
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().registerOperator(x).and(y).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor().getOperatorContext(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor().getOperatorContext().getBySuffix("X"), is(notNullValue()));
        assertThat(factory.getConfiguration().getDescriptionExtractor().getOperatorContext().getBySuffix("X"), Matchers.<Operator>is(x));
        assertThat(factory.getConfiguration().getDescriptionExtractor().getOperatorContext().getBySuffix("Y"), is(notNullValue(Operator.class)));
        assertThat(factory.getConfiguration().getDescriptionExtractor().getOperatorContext().getBySuffix("Y"), Matchers.<Operator>is(y));
    }

    @Test
    public void testUsingCustomFunctionRegistry() throws Exception {
        final DefaultDataFunctionRegistry functionRegistry = new DefaultDataFunctionRegistry();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withDataFunctions(functionRegistry).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getFunctionRegistry(), is(notNullValue()));
        assertThat(factory.getConfiguration().getFunctionRegistry(), Matchers.<DataFunctionRegistry>is(functionRegistry));
    }

    @Test
    public void testUsingDefaultFunctionRegistryWithExtraFunctions() throws Exception {
        final SpyingDataFunction<Object> x = new SpyingDataFunction<>(null);
        final SpyingDataFunction<Object> y = new SpyingDataFunction<>(null);
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().registerFunction("x", x).and("y", y).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getFunctionRegistry(), is(notNullValue()));
        assertThat(factory.getConfiguration().getFunctionRegistry(), is(notNullValue()));
        assertThat(factory.getConfiguration().getFunctionRegistry().getFunction("x"), is(notNullValue()));
        assertThat(factory.getConfiguration().getFunctionRegistry().getFunction("x"), Matchers.<DataFunction>is(x));
        assertThat(factory.getConfiguration().getFunctionRegistry().getFunction("y"), is(notNullValue()));
        assertThat(factory.getConfiguration().getFunctionRegistry().getFunction("y"), Matchers.<DataFunction>is(y));
    }

    @Test
    public void testUsingCustomDataStoreRegistry() throws Exception {
        final DefaultDataStoreRegistry registry = new DefaultDataStoreRegistry();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withDataStores(registry).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDataStoreRegistry(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDataStoreRegistry(), Matchers.<DataStoreRegistry>is(registry));
    }

    @Test
    public void testUsingDefaultDataStoreRegistryAndCustomDataStores() throws Exception {
        final MemoryDataStore<Serializable, Integer> x = new MemoryDataStore<>(Integer.class);
        final MemoryDataStore<Serializable, String> y = new MemoryDataStore<>(String.class);
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().registerDataStore(x).and(y).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDataStoreRegistry(), is(notNullValue()));
        assertThat(factory.getConfiguration().getDataStoreRegistry().getDataStore(Integer.class), is(notNullValue()));
        assertThat(factory.getConfiguration().getDataStoreRegistry().getDataStore(Integer.class), Matchers.<DataStore>is(x));
        assertThat(factory.getConfiguration().getDataStoreRegistry().getDataStore(String.class), is(notNullValue()));
        assertThat(factory.getConfiguration().getDataStoreRegistry().getDataStore(String.class), Matchers.<DataStore>is(y));
    }

    @Test
    public void testUsingCustomResultAdapterContext() throws Exception {
        final DefaultResultAdapterContext context = new DefaultResultAdapterContext();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withAdapters(context).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getResultAdapterContext(), is(notNullValue()));
        assertThat(factory.getConfiguration().getResultAdapterContext(), Matchers.<ResultAdapterContext>is(context));
    }

    @Test
    public void testUsingDefaultContextWithCustomAdapters() throws Exception {
        final VoidResultAdapter x = new VoidResultAdapter();
        final VoidResultAdapter y = new VoidResultAdapter();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().adaptResultsUsing(x).and(y).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getResultAdapterContext(), is(notNullValue()));
        assertThat(factory.getConfiguration().getResultAdapterContext().getAdapters(), hasItem(x));
        assertThat(factory.getConfiguration().getResultAdapterContext().getAdapters(), hasItem(y));
    }

    @Test
    public void testUsingCustomTypeMappingContext() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withMappings(context).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getTypeMappingContext(), is(notNullValue()));
        assertThat(factory.getConfiguration().getTypeMappingContext(), Matchers.<TypeMappingContext>is(context));
    }

    @Test
    public void testUsingDefaultTypeMappingContextAndCustomTypeMappings() throws Exception {
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().honoringImplementation(Object.class, Integer.class).and(Object.class, String.class).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getTypeMappingContext(), is(notNullValue()));
        assertThat(factory.getConfiguration().getTypeMappingContext().getImplementations(Object.class), hasItem(Integer.class));
        assertThat(factory.getConfiguration().getTypeMappingContext().getImplementations(Object.class), hasItem(String.class));
    }

    @Test
    public void testUsingCustomOperationHandler() throws Exception {
        final NonDataOperationInvocationHandler handler = new NonDataOperationInvocationHandler();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withOperationHandlers(handler).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getOperationInvocationHandler(), is(notNullValue()));
        assertThat(factory.getConfiguration().getOperationInvocationHandler(), is(handler));
    }

    @Test
    public void testUsingDefaultOperationHandlerWithCustomOperations() throws Exception {
        final SpyingHandler x = new SpyingHandler();
        final SpyingHandler y = new SpyingHandler();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withOperationHandler(x).and(y).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getOperationInvocationHandler(), is(notNullValue()));
        assertThat(factory.getConfiguration().getOperationInvocationHandler().getHandlers(), hasItem(x));
        assertThat(factory.getConfiguration().getOperationInvocationHandler().getHandlers(), hasItem(y));
    }

    @Test
    public void testUsingCustomEventListenerContext() throws Exception {
        final DefaultDataStoreEventListenerContext context = new DefaultDataStoreEventListenerContext();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withListeners(context).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getEventListenerContext(), is(notNullValue()));
        assertThat(factory.getConfiguration().getEventListenerContext(), Matchers.<DataStoreEventListenerContext>is(context));
    }

    @Test
    public void testUsingDefaultEventListenerContextWithCustomListeners() throws Exception {
        final AllCatchingEventListener x = new AllCatchingEventListener();
        final AllCatchingEventListener y = new AllCatchingEventListener();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().withListener(x).and(y).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        assertThat(factory.getConfiguration().getEventListenerContext(), is(notNullValue()));
        assertThat(factory.getConfiguration().getEventListenerContext().getListeners(DataStoreEvent.class), hasItem(x));
        assertThat(factory.getConfiguration().getEventListenerContext().getListeners(DataStoreEvent.class), hasItem(y));
    }

    @Test
    public void testEnablingAuditing() throws Exception {
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().enableAuditing().build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        final DataStoreEventListenerContext listenerContext = factory.getConfiguration().getEventListenerContext();
        assertThat(listenerContext, is(notNullValue()));
        final List<DataStoreEventListener<? extends DataStoreEvent>> listeners = listenerContext.getListeners(DataStoreEvent.class);
        assertThat(listeners, hasSize(1));
        assertThat(listeners.get(0), is(instanceOf(AuditDataEventListener.class)));
    }

    @Test
    public void testEnablingAuditingWithCustomAuditorAware() throws Exception {
        final RepositoryFactoryBuilder.DefaultAuditorAware auditorAware = new RepositoryFactoryBuilder.DefaultAuditorAware();
        final RepositoryFactory factory = RepositoryFactoryBuilder.builder().enableAuditing(auditorAware).build();
        assertThat(factory, is(notNullValue()));
        assertThat(factory.getConfiguration(), is(notNullValue()));
        final DataStoreEventListenerContext listenerContext = factory.getConfiguration().getEventListenerContext();
        assertThat(listenerContext, is(notNullValue()));
        final List<DataStoreEventListener<? extends DataStoreEvent>> listeners = listenerContext.getListeners(DataStoreEvent.class);
        assertThat(listeners, hasSize(1));
        assertThat(listeners.get(0), is(instanceOf(AuditDataEventListener.class)));
        final AuditDataEventListener auditDataEventListener = (AuditDataEventListener) listeners.get(0);
        final AuditorAware usedAuditorAware = auditDataEventListener.getAuditorAware();
        assertThat(usedAuditorAware, is(notNullValue()));
        assertThat(usedAuditorAware, Matchers.<AuditorAware>is(auditorAware));
    }

    @Test
    public void testDefaultAuditorAware() throws Exception {
        final RepositoryFactoryBuilder.DefaultAuditorAware auditorAware = new RepositoryFactoryBuilder.DefaultAuditorAware();
        assertThat(auditorAware.getCurrentAuditor(), is(RepositoryFactoryBuilder.DEFAULT_USER));
    }

    @Test
    public void testOutOfTheBoxMocking() throws Exception {
        final SimplePersonRepository repository = RepositoryFactoryBuilder.builder().mock(SimplePersonRepository.class);
        assertThat(repository, is(notNullValue()));
    }

    @Test
    public void testMockingUsingCustomImplementation() throws Exception {
        final ExtendedSimplePersonRepository repository = RepositoryFactoryBuilder.builder().usingImplementation(StringMapping.class).and(NumberMapping.class).mock(ExtendedSimplePersonRepository.class);
        assertThat(repository, is(notNullValue()));
        assertThat(repository.getString(), is("Hello!"));
        assertThat(repository.getNumber(), Matchers.<Number>is(123));
    }

    @Test
    public void testMockingWithoutGeneratingKeys() throws Exception {
        final ConfiguredSimplePersonRepository repository = RepositoryFactoryBuilder.builder().withoutGeneratingKeys().usingImplementation(ConfiguredMapping.class).mock(ConfiguredSimplePersonRepository.class);
        assertThat(repository.getRepositoryConfiguration(), is(notNullValue()));
        assertThat(repository.getRepositoryConfiguration().getKeyGenerator(), is(nullValue()));
    }

    @Test
    public void testMockingWithCustomKeyGeneration() throws Exception {
        final RepositoryMockBuilder.NoOpKeyGenerator<Serializable> keyGenerator = new RepositoryMockBuilder.NoOpKeyGenerator<>();
        final ConfiguredSimplePersonRepository repository = RepositoryFactoryBuilder.builder().generateKeysUsing(keyGenerator).usingImplementation(ConfiguredMapping.class).mock(ConfiguredSimplePersonRepository.class);
        assertThat(repository.getRepositoryConfiguration(), is(notNullValue()));
        assertThat(repository.getRepositoryConfiguration().getKeyGenerator(), is(notNullValue()));
        assertThat(repository.getRepositoryConfiguration().getKeyGenerator(), Matchers.<KeyGenerator<?>>is(keyGenerator));
    }

    @Test
    public void testMockingWithCustomKeyGenerationByType() throws Exception {
        //noinspection unchecked
        final ConfiguredSimplePersonRepository repository = RepositoryFactoryBuilder.builder().generateKeysUsing(RepositoryMockBuilder.NoOpKeyGenerator.class).usingImplementation(ConfiguredMapping.class).mock(ConfiguredSimplePersonRepository.class);
        assertThat(repository.getRepositoryConfiguration(), is(notNullValue()));
        assertThat(repository.getRepositoryConfiguration().getKeyGenerator(), is(notNullValue()));
        assertThat(repository.getRepositoryConfiguration().getKeyGenerator(), is(instanceOf(RepositoryMockBuilder.NoOpKeyGenerator.class)));
    }

}