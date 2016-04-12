package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryAware;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.proxy.*;
import com.mmnaseri.utils.spring.data.query.impl.DefaultDataFunctionRegistry;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreEventListenerContext;
import com.mmnaseri.utils.spring.data.store.impl.DefaultDataStoreRegistry;
import org.springframework.data.repository.Repository;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultRepositoryFactoryTest {

    @Test
    public void testRepositoryInstance() throws Exception {
        final DefaultRepositoryFactoryConfiguration configuration = new DefaultRepositoryFactoryConfiguration();
        final DefaultDataStoreRegistry dataStoreRegistry = new DefaultDataStoreRegistry();
        configuration.setDataStoreRegistry(dataStoreRegistry);
        configuration.setDescriptionExtractor(new QueryDescriptionExtractor(new DefaultOperatorContext()));
        configuration.setEventListenerContext(new DefaultDataStoreEventListenerContext());
        configuration.setFunctionRegistry(new DefaultDataFunctionRegistry());
        configuration.setOperationInvocationHandler(new NonDataOperationInvocationHandler());
        configuration.setRepositoryMetadataResolver(new DefaultRepositoryMetadataResolver());
        configuration.setResultAdapterContext(new DefaultResultAdapterContext());
        configuration.setTypeMappingContext(new DefaultTypeMappingContext());
        final DefaultRepositoryFactory factory = new DefaultRepositoryFactory(configuration);
        assertThat(factory.getConfiguration(), is(configuration));
        assertThat(dataStoreRegistry.has(Person.class), is(false));
        factory.getInstance(new UUIDKeyGenerator(), SampleRepository.class, RepositoryClearerMapping.class);
        assertThat(dataStoreRegistry.has(Person.class), is(true));
        final SampleRepository repository = factory.getInstance(new UUIDKeyGenerator(), SampleRepository.class, RepositoryClearerMapping.class);
        final DataStore<Serializable, Person> dataStore = dataStoreRegistry.getDataStore(Person.class);
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

    public interface SampleRepository extends Repository<Person, String> {

        List<Person> findAll();

        List<Person> deleteAll();

        void clearRepo();

        List<Person> findByLastName(String lastName);

    }

    public static class RepositoryClearerMapping implements RepositoryAware<SampleRepository>, RepositoryFactoryConfigurationAware, RepositoryConfigurationAware, RepositoryFactoryAware {

        private SampleRepository repository;

        @Override
        public void setRepository(SampleRepository repository) {
            this.repository = repository;
        }

        public void clearRepo() {
            repository.deleteAll();
        }

        @Override
        public void setRepositoryFactoryConfiguration(RepositoryFactoryConfiguration configuration) {
            assertThat(configuration, is(notNullValue()));
        }

        @Override
        public void setRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration) {
            assertThat(repositoryConfiguration, is(notNullValue()));
            assertThat(repositoryConfiguration.getKeyGenerator(), is(notNullValue()));
            assertThat(repositoryConfiguration.getKeyGenerator(), is(instanceOf(UUIDKeyGenerator.class)));
            assertThat(repositoryConfiguration.getRepositoryMetadata(), is(notNullValue()));
            assertThat(repositoryConfiguration.getRepositoryMetadata().getEntityType(), is(equalTo(Person.class)));
            assertThat(repositoryConfiguration.getRepositoryMetadata().getIdentifierProperty(), is("id"));
            assertThat(repositoryConfiguration.getRepositoryMetadata().getIdentifierType(), is(equalTo(String.class)));
            assertThat(repositoryConfiguration.getRepositoryMetadata().getRepositoryInterface(), is(equalTo(SampleRepository.class)));
            assertThat(repositoryConfiguration.getBoundImplementations(), is(not(empty())));
            assertThat(repositoryConfiguration.getBoundImplementations(), hasItem(RepositoryClearerMapping.class));
        }

        @Override
        public void setRepositoryFactory(RepositoryFactory factory) {
            assertThat(factory, is(notNullValue()));
        }
    }

}