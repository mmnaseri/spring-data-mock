package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.key.NoOpKeyGenerator;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.ClearableSimpleCrudPersonRepository;
import com.mmnaseri.utils.spring.data.sample.repositories.RepositoryClearerMapping;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class DefaultRepositoryFactoryTest {

    @Test
    public void testRepositoryInstance() throws Exception {
        final DefaultRepositoryFactoryConfiguration configuration = new DefaultRepositoryFactoryConfiguration();
        final DefaultDataStoreRegistry dataStoreRegistry = new DefaultDataStoreRegistry();
        configuration.setDataStoreRegistry(dataStoreRegistry);
        configuration.setDescriptionExtractor(new MethodQueryDescriptionExtractor(new DefaultOperatorContext()));
        configuration.setEventListenerContext(new DefaultDataStoreEventListenerContext());
        configuration.setFunctionRegistry(new DefaultDataFunctionRegistry());
        configuration.setOperationInvocationHandler(new NonDataOperationInvocationHandler());
        configuration.setRepositoryMetadataResolver(new DefaultRepositoryMetadataResolver());
        configuration.setResultAdapterContext(new DefaultResultAdapterContext());
        configuration.setTypeMappingContext(new DefaultTypeMappingContext());
        configuration.setDefaultKeyGenerator(null);
        final DefaultRepositoryFactory factory = new DefaultRepositoryFactory(configuration);
        assertThat(factory.getConfiguration(), Matchers.<RepositoryFactoryConfiguration>is(configuration));
        assertThat(dataStoreRegistry.has(Person.class), is(false));
        factory.getInstance(null, ClearableSimpleCrudPersonRepository.class, RepositoryClearerMapping.class);
        assertThat(dataStoreRegistry.has(Person.class), is(true));
        final ClearableSimpleCrudPersonRepository repository = factory.getInstance(null, ClearableSimpleCrudPersonRepository.class, RepositoryClearerMapping.class);
        final DataStore<Object, Person> dataStore = dataStoreRegistry.getDataStore(Person.class);
        dataStore.save("k1", new Person().setId("k1").setLastName("Sadeghi"));
        dataStore.save("k2", new Person().setId("k2").setLastName("Naseri"));
        dataStore.save("k3", new Person().setId("k3").setLastName("Sadeghi"));
        dataStore.save("k4", new Person().setId("k4").setLastName("Naseri"));
        assertThat(repository.findAll(), containsInAnyOrder(dataStore.retrieveAll().toArray()));
        assertThat(repository.findByLastName("Naseri"), containsInAnyOrder(dataStore.retrieve("k2"), dataStore.retrieve("k4")));
        assertThat(repository.findByLastName("Sadeghi"), containsInAnyOrder(dataStore.retrieve("k1"), dataStore.retrieve("k3")));
        assertThat(repository.findByLastName("Ghomboli"), is(empty()));
        final Collection<Person> all = dataStore.retrieveAll();
        assertThat(repository.deleteAll(), containsInAnyOrder(all.toArray()));
        dataStore.save("k1", new Person().setId("k1").setLastName("Sadeghi"));
        dataStore.save("k2", new Person().setId("k2").setLastName("Naseri"));
        dataStore.save("k3", new Person().setId("k3").setLastName("Sadeghi"));
        dataStore.save("k4", new Person().setId("k4").setLastName("Naseri"));
        assertThat(dataStore.retrieveAll(), hasSize(4));
        repository.clearRepo();
        assertThat(dataStore.retrieveAll(), is(empty()));
    }

    @Test
    public void testRepositoryInstanceWithKeyGenerationFallback() throws Exception {
        final DefaultRepositoryFactoryConfiguration configuration = new DefaultRepositoryFactoryConfiguration();
        final DefaultDataStoreRegistry dataStoreRegistry = new DefaultDataStoreRegistry();
        configuration.setDataStoreRegistry(dataStoreRegistry);
        configuration.setDescriptionExtractor(new MethodQueryDescriptionExtractor(new DefaultOperatorContext()));
        configuration.setEventListenerContext(new DefaultDataStoreEventListenerContext());
        configuration.setFunctionRegistry(new DefaultDataFunctionRegistry());
        configuration.setOperationInvocationHandler(new NonDataOperationInvocationHandler());
        configuration.setRepositoryMetadataResolver(new DefaultRepositoryMetadataResolver());
        configuration.setResultAdapterContext(new DefaultResultAdapterContext());
        configuration.setTypeMappingContext(new DefaultTypeMappingContext());
        configuration.setDefaultKeyGenerator(new NoOpKeyGenerator<>());
        final DefaultRepositoryFactory factory = new DefaultRepositoryFactory(configuration);
        assertThat(factory.getConfiguration(), Matchers.<RepositoryFactoryConfiguration>is(configuration));
        assertThat(dataStoreRegistry.has(Person.class), is(false));
        factory.getInstance(null, ClearableSimpleCrudPersonRepository.class, RepositoryClearerMapping.class);
        assertThat(dataStoreRegistry.has(Person.class), is(true));
        final ClearableSimpleCrudPersonRepository repository = factory.getInstance(null, ClearableSimpleCrudPersonRepository.class, RepositoryClearerMapping.class);
        final DataStore<Object, Person> dataStore = dataStoreRegistry.getDataStore(Person.class);
        dataStore.save("k1", new Person().setId("k1").setLastName("Sadeghi"));
        dataStore.save("k2", new Person().setId("k2").setLastName("Naseri"));
        dataStore.save("k3", new Person().setId("k3").setLastName("Sadeghi"));
        dataStore.save("k4", new Person().setId("k4").setLastName("Naseri"));
        assertThat(repository.findAll(), containsInAnyOrder(dataStore.retrieveAll().toArray()));
        assertThat(repository.findByLastName("Naseri"), containsInAnyOrder(dataStore.retrieve("k2"), dataStore.retrieve("k4")));
        assertThat(repository.findByLastName("Sadeghi"), containsInAnyOrder(dataStore.retrieve("k1"), dataStore.retrieve("k3")));
        assertThat(repository.findByLastName("Ghomboli"), is(empty()));
        final Collection<Person> all = dataStore.retrieveAll();
        assertThat(repository.deleteAll(), containsInAnyOrder(all.toArray()));
        dataStore.save("k1", new Person().setId("k1").setLastName("Sadeghi"));
        dataStore.save("k2", new Person().setId("k2").setLastName("Naseri"));
        dataStore.save("k3", new Person().setId("k3").setLastName("Sadeghi"));
        dataStore.save("k4", new Person().setId("k4").setLastName("Naseri"));
        assertThat(dataStore.retrieveAll(), hasSize(4));
        repository.clearRepo();
        assertThat(dataStore.retrieveAll(), is(empty()));
    }

}