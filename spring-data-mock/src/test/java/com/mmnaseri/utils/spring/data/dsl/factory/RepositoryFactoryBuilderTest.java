package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableOperator;
import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.key.NoOpKeyGenerator;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultTypeMappingContext;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;
import com.mmnaseri.utils.spring.data.proxy.impl.adapters.VoidResultAdapter;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.sample.mocks.AllCatchingEventListener;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingDataFunction;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingHandler;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.ConfiguredMapping;
import com.mmnaseri.utils.spring.data.sample.repositories.ConfiguredSimplePersonRepository;
import com.mmnaseri.utils.spring.data.sample.repositories.ExtendedSimplePersonRepository;
import com.mmnaseri.utils.spring.data.sample.repositories.JpaPersonRepository;
import com.mmnaseri.utils.spring.data.sample.repositories.NumberMapping;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.sample.repositories.StringMapping;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.AuditDataEventListener;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.springframework.data.domain.AuditorAware;
import org.springframework.data.jpa.repository.JpaRepository;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;

import javax.persistence.Id;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.given;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 11:42 AM)
 */
public class RepositoryFactoryBuilderTest {

  @Test
  public void testDefaultConfiguration() {
    final RepositoryFactoryConfiguration configuration =
        RepositoryFactoryBuilder.defaultConfiguration();
    assertThat(configuration, is(notNullValue()));
    assertThat(configuration.getDataStoreRegistry(), is(notNullValue()));
    assertThat(configuration.getTypeMappingContext(), is(notNullValue()));
    assertThat(configuration.getResultAdapterContext(), is(notNullValue()));
    assertThat(configuration.getRepositoryMetadataResolver(), is(notNullValue()));
    assertThat(configuration.getOperationInvocationHandler(), is(notNullValue()));
    assertThat(configuration.getFunctionRegistry(), is(notNullValue()));
    assertThat(configuration.getEventListenerContext(), is(notNullValue()));
    assertThat(configuration.getDescriptionExtractor(), is(notNullValue()));
  }

  @Test
  public void testDefaultFactory() {
    final RepositoryFactory factory = RepositoryFactoryBuilder.defaultFactory();
    assertThat(factory, is(notNullValue()));
  }

  @Test
  public void testConfiguringAProvidedConfiguration() {
    final DefaultRepositoryFactoryConfiguration configuration =
        new DefaultRepositoryFactoryConfiguration();
    final DefaultDataFunctionRegistry functionRegistry = new DefaultDataFunctionRegistry();
    configuration.setFunctionRegistry(functionRegistry);
    configuration.setDefaultKeyGenerator(new NoOpKeyGenerator<>());
    final UUIDKeyGenerator keyGenerator = new UUIDKeyGenerator();
    final RepositoryFactoryConfiguration modifiedConfiguration =
        given(configuration)
            .withDefaultKeyGenerator(keyGenerator)
            .withListener(new AllCatchingEventListener())
            .configure();
    assertThat(modifiedConfiguration, is(notNullValue()));
    assertThat(modifiedConfiguration.getDefaultKeyGenerator(), is((Object) keyGenerator));
    assertThat(modifiedConfiguration.getEventListenerContext(), is(notNullValue()));
    assertThat(
        modifiedConfiguration.getEventListenerContext().getListeners(DataStoreEvent.class),
        hasSize(1));
  }

  @Test
  public void testUsingCustomMetadataResolver() {
    final DefaultRepositoryMetadataResolver resolver = new DefaultRepositoryMetadataResolver();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().resolveMetadataUsing(resolver).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getRepositoryMetadataResolver(), is(notNullValue()));
    assertThat(factory.getConfiguration().getRepositoryMetadataResolver(), Matchers.is(resolver));
  }

  @Test
  public void testUsingCustomQueryDescriptor() {
    final MethodQueryDescriptionExtractor queryDescriptionExtractor =
        new MethodQueryDescriptionExtractor(new DefaultOperatorContext());
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().extractQueriesUsing(queryDescriptionExtractor).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getDescriptionExtractor(), is(notNullValue()));
    assertThat(factory.getConfiguration().getDescriptionExtractor(), is(queryDescriptionExtractor));
  }

  @Test
  public void testSpecifyingDefaultKeyGenerator() {
    final UUIDKeyGenerator generator = new UUIDKeyGenerator();
    final RepositoryFactoryConfiguration configuration =
        RepositoryFactoryBuilder.builder().withDefaultKeyGenerator(generator).configure();
    assertThat(configuration.getDefaultKeyGenerator(), is(notNullValue()));
    assertThat(configuration.getDefaultKeyGenerator(), is((Object) generator));
  }

  @Test
  public void testUsingCustomOperatorContext() {
    final DefaultOperatorContext operatorContext = new DefaultOperatorContext();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withOperators(operatorContext).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getDescriptionExtractor(), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getDescriptionExtractor().getOperatorContext(),
        is(notNullValue()));
    assertThat(
        factory.getConfiguration().getDescriptionExtractor().getOperatorContext(),
        Matchers.is(operatorContext));
  }

  @Test
  public void testUsingDefaultOperatorContextWithAdditionalOperators() {
    final ImmutableOperator x = new ImmutableOperator("x", 0, null, "X");
    final ImmutableOperator y = new ImmutableOperator("y", 0, null, "Y");
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().registerOperator(x).and(y).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getDescriptionExtractor(), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getDescriptionExtractor().getOperatorContext(),
        is(notNullValue()));
    assertThat(
        factory.getConfiguration().getDescriptionExtractor().getOperatorContext().getBySuffix("X"),
        is(notNullValue()));
    assertThat(
        factory.getConfiguration().getDescriptionExtractor().getOperatorContext().getBySuffix("X"),
        Matchers.is(x));
    assertThat(
        factory.getConfiguration().getDescriptionExtractor().getOperatorContext().getBySuffix("Y"),
        is(notNullValue(Operator.class)));
    assertThat(
        factory.getConfiguration().getDescriptionExtractor().getOperatorContext().getBySuffix("Y"),
        Matchers.is(y));
  }

  @Test
  public void testUsingCustomFunctionRegistry() {
    final DefaultDataFunctionRegistry functionRegistry = new DefaultDataFunctionRegistry();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withDataFunctions(functionRegistry).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getFunctionRegistry(), is(notNullValue()));
    assertThat(factory.getConfiguration().getFunctionRegistry(), Matchers.is(functionRegistry));
  }

  @Test
  public void testUsingDefaultFunctionRegistryWithExtraFunctions() {
    final SpyingDataFunction<Object> x = new SpyingDataFunction<>(null);
    final SpyingDataFunction<Object> y = new SpyingDataFunction<>(null);
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().registerFunction("x", x).and("y", y).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getFunctionRegistry(), is(notNullValue()));
    assertThat(factory.getConfiguration().getFunctionRegistry(), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getFunctionRegistry().getFunction("x"), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getFunctionRegistry().getFunction("x"),
        Matchers.<DataFunction>is(x));
    assertThat(
        factory.getConfiguration().getFunctionRegistry().getFunction("y"), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getFunctionRegistry().getFunction("y"),
        Matchers.<DataFunction>is(y));
  }

  @Test
  public void testUsingCustomDataStoreRegistry() {
    final DefaultDataStoreRegistry registry = new DefaultDataStoreRegistry();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withDataStores(registry).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getDataStoreRegistry(), is(notNullValue()));
    assertThat(factory.getConfiguration().getDataStoreRegistry(), Matchers.is(registry));
  }

  @Test
  public void testUsingDefaultDataStoreRegistryAndCustomDataStores() {
    final MemoryDataStore<Object, Integer> x = new MemoryDataStore<>(Integer.class);
    final MemoryDataStore<Object, String> y = new MemoryDataStore<>(String.class);
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().registerDataStore(x).and(y).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getDataStoreRegistry(), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getDataStoreRegistry().getDataStore(Integer.class),
        is(notNullValue()));
    assertThat(
        factory.getConfiguration().getDataStoreRegistry().getDataStore(Integer.class),
        Matchers.<DataStore>is(x));
    assertThat(
        factory.getConfiguration().getDataStoreRegistry().getDataStore(String.class),
        is(notNullValue()));
    assertThat(
        factory.getConfiguration().getDataStoreRegistry().getDataStore(String.class),
        Matchers.<DataStore>is(y));
  }

  @Test
  public void testUsingCustomResultAdapterContext() {
    final DefaultResultAdapterContext context = new DefaultResultAdapterContext();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withAdapters(context).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getResultAdapterContext(), is(notNullValue()));
    assertThat(factory.getConfiguration().getResultAdapterContext(), Matchers.is(context));
  }

  @Test
  public void testUsingDefaultContextWithCustomAdapters() {
    final VoidResultAdapter x = new VoidResultAdapter();
    final VoidResultAdapter y = new VoidResultAdapter();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().adaptResultsUsing(x).and(y).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getResultAdapterContext(), is(notNullValue()));
    assertThat(factory.getConfiguration().getResultAdapterContext().getAdapters(), hasItem(x));
    assertThat(factory.getConfiguration().getResultAdapterContext().getAdapters(), hasItem(y));
  }

  @Test
  public void testUsingCustomTypeMappingContext() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withMappings(context).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getTypeMappingContext(), is(notNullValue()));
    assertThat(factory.getConfiguration().getTypeMappingContext(), Matchers.is(context));
  }

  @Test
  public void testUsingDefaultTypeMappingContextAndCustomTypeMappings() {
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder()
            .honoringImplementation(Object.class, Integer.class)
            .and(Object.class, String.class)
            .build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getTypeMappingContext(), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getTypeMappingContext().getImplementations(Object.class),
        hasItem(Integer.class));
    assertThat(
        factory.getConfiguration().getTypeMappingContext().getImplementations(Object.class),
        hasItem(String.class));
  }

  @Test
  public void testUsingCustomOperationHandler() {
    final NonDataOperationInvocationHandler handler = new NonDataOperationInvocationHandler();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withOperationHandlers(handler).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getOperationInvocationHandler(), is(notNullValue()));
    assertThat(factory.getConfiguration().getOperationInvocationHandler(), is(handler));
  }

  @Test
  public void testUsingDefaultOperationHandlerWithCustomOperations() {
    final SpyingHandler x = new SpyingHandler();
    final SpyingHandler y = new SpyingHandler();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withOperationHandler(x).and(y).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getOperationInvocationHandler(), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getOperationInvocationHandler().getHandlers(), hasItem(x));
    assertThat(
        factory.getConfiguration().getOperationInvocationHandler().getHandlers(), hasItem(y));
  }

  @Test
  public void testUsingCustomEventListenerContext() {
    final DefaultDataStoreEventListenerContext context = new DefaultDataStoreEventListenerContext();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withListeners(context).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getEventListenerContext(), is(notNullValue()));
    assertThat(factory.getConfiguration().getEventListenerContext(), Matchers.is(context));
  }

  @Test
  public void testUsingDefaultEventListenerContextWithCustomListeners() {
    final AllCatchingEventListener x = new AllCatchingEventListener();
    final AllCatchingEventListener y = new AllCatchingEventListener();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().withListener(x).and(y).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    assertThat(factory.getConfiguration().getEventListenerContext(), is(notNullValue()));
    assertThat(
        factory.getConfiguration().getEventListenerContext().getListeners(DataStoreEvent.class),
        hasItem(x));
    assertThat(
        factory.getConfiguration().getEventListenerContext().getListeners(DataStoreEvent.class),
        hasItem(y));
  }

  @Test
  public void testEnablingAuditing() {
    final RepositoryFactory factory = RepositoryFactoryBuilder.builder().enableAuditing().build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    final DataStoreEventListenerContext listenerContext =
        factory.getConfiguration().getEventListenerContext();
    assertThat(listenerContext, is(notNullValue()));
    final List<DataStoreEventListener<? extends DataStoreEvent>> listeners =
        listenerContext.getListeners(DataStoreEvent.class);
    assertThat(listeners, hasSize(1));
    assertThat(listeners.get(0), is(instanceOf(AuditDataEventListener.class)));
  }

  @Test
  public void testEnablingAuditingWithCustomAuditorAware() {
    final AuditorAware auditorAware = new RepositoryFactoryBuilder.DefaultAuditorAware();
    final RepositoryFactory factory =
        RepositoryFactoryBuilder.builder().enableAuditing(auditorAware).build();
    assertThat(factory, is(notNullValue()));
    assertThat(factory.getConfiguration(), is(notNullValue()));
    final DataStoreEventListenerContext listenerContext =
        factory.getConfiguration().getEventListenerContext();
    assertThat(listenerContext, is(notNullValue()));
    final List<DataStoreEventListener<? extends DataStoreEvent>> listeners =
        listenerContext.getListeners(DataStoreEvent.class);
    assertThat(listeners, hasSize(1));
    assertThat(listeners.get(0), is(instanceOf(AuditDataEventListener.class)));
    final AuditDataEventListener auditDataEventListener = (AuditDataEventListener) listeners.get(0);
    final AuditorAware usedAuditorAware = auditDataEventListener.getAuditorAware();
    assertThat(usedAuditorAware, is(notNullValue()));
    assertThat(usedAuditorAware, is(auditorAware));
  }

  @Test
  public void testDefaultAuditorAware() {
    final AuditorAware<String> auditorAware = new RepositoryFactoryBuilder.DefaultAuditorAware();
    assertThat(auditorAware.getCurrentAuditor().isPresent(), is(true));
    assertThat(auditorAware.getCurrentAuditor().get(), is(RepositoryFactoryBuilder.DEFAULT_USER));
  }

  @Test
  public void testOutOfTheBoxMocking() {
    final SimplePersonRepository repository =
        RepositoryFactoryBuilder.builder().mock(SimplePersonRepository.class);
    assertThat(repository, is(notNullValue()));
  }

  @Test
  public void testMockingUsingCustomImplementation() {
    final ExtendedSimplePersonRepository repository =
        RepositoryFactoryBuilder.builder()
            .usingImplementation(StringMapping.class)
            .and(NumberMapping.class)
            .mock(ExtendedSimplePersonRepository.class);
    assertThat(repository, is(notNullValue()));
    assertThat(repository.getString(), is("Hello!"));
    assertThat(repository.getNumber(), Matchers.is(123));
  }

  @Test
  public void testMockingWithoutGeneratingKeys() {
    final ConfiguredSimplePersonRepository repository =
        RepositoryFactoryBuilder.builder()
            .withoutGeneratingKeys()
            .usingImplementation(ConfiguredMapping.class)
            .mock(ConfiguredSimplePersonRepository.class);
    assertThat(repository.getRepositoryConfiguration(), is(notNullValue()));
    assertThat(repository.getRepositoryConfiguration().getKeyGenerator(), is(notNullValue()));
    assertThat(
        repository.getRepositoryConfiguration().getKeyGenerator(),
        is(instanceOf(NoOpKeyGenerator.class)));
  }

  @Test
  public void testMockingWithCustomKeyGeneration() {
    final NoOpKeyGenerator<Object> keyGenerator = new NoOpKeyGenerator<>();
    final ConfiguredSimplePersonRepository repository =
        RepositoryFactoryBuilder.builder()
            .generateKeysUsing(keyGenerator)
            .usingImplementation(ConfiguredMapping.class)
            .mock(ConfiguredSimplePersonRepository.class);
    assertThat(repository.getRepositoryConfiguration(), is(notNullValue()));
    assertThat(repository.getRepositoryConfiguration().getKeyGenerator(), is(notNullValue()));
    assertThat(
        repository.getRepositoryConfiguration().getKeyGenerator(), Matchers.is(keyGenerator));
  }

  @Test
  public void testMockingWithCustomKeyGenerationByType() {
    //noinspection unchecked
    final ConfiguredSimplePersonRepository repository =
        RepositoryFactoryBuilder.builder()
            .generateKeysUsing(NoOpKeyGenerator.class)
            .usingImplementation(ConfiguredMapping.class)
            .mock(ConfiguredSimplePersonRepository.class);
    assertThat(repository.getRepositoryConfiguration(), is(notNullValue()));
    assertThat(repository.getRepositoryConfiguration().getKeyGenerator(), is(notNullValue()));
    assertThat(
        repository.getRepositoryConfiguration().getKeyGenerator(),
        is(instanceOf(NoOpKeyGenerator.class)));
  }

  @Test
  public void testSaveIterable() {
    final JpaPersonRepository repository =
        RepositoryFactoryBuilder.builder().mock(JpaPersonRepository.class);
    final Iterable iterable =
        Arrays.asList(new Person().setId("1"), new Person().setId("2"), new Person().setId("3"));
    repository.saveAll(iterable);
    assertThat(repository.findAll(), hasSize(3));
    assertThat(repository.findAll(), containsInAnyOrder(((List) iterable).toArray()));
  }

  // please move the code where it should be
  public class EntityWithAnnotatedIdFieldAndGetter {
      @Id
      private Long id;
      public Long getId() {
		return id;
	}
  }

  // please move the code where it should be
  public interface RepositoryForIssue extends JpaRepository<EntityWithAnnotatedIdFieldAndGetter, Long> {
  }

  // please move the code where it should be
  @Test
  public void privateIdFieldIssue() {
	  final RepositoryForIssue repository =
			  RepositoryFactoryBuilder.builder().mock(RepositoryForIssue.class);
	  repository.save(new EntityWithAnnotatedIdFieldAndGetter());
	  assertThat(repository.findAll(), hasSize(1));
  }
}
