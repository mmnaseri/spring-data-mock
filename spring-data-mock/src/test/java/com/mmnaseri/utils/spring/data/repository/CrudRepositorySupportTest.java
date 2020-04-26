package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.error.DataStoreException;
import com.mmnaseri.utils.spring.data.sample.mocks.Operation;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingDataStore;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/11/16, 10:15 AM)
 */
public class CrudRepositorySupportTest {

    @Test
    public void testIntegrity() {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final MemoryDataStore<Object, Object> dataStore = new MemoryDataStore<>(Object.class);
        support.setDataStore(dataStore);
        final ImmutableRepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class,
                                                                                               Person.class,
                                                                                               SimplePersonRepository.class,
                                                                                               "id");
        support.setRepositoryMetadata(repositoryMetadata);
        final UUIDKeyGenerator keyGenerator = new UUIDKeyGenerator();
        support.setKeyGenerator(keyGenerator);
        assertThat(support.getDataStore(), Matchers.is(dataStore));
        assertThat(support.getKeyGenerator(), Matchers.<KeyGenerator>is(keyGenerator));
        assertThat(support.getRepositoryMetadata(), Matchers.is(repositoryMetadata));
    }

    @Test
    public void testPerformingUpdates() {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final SpyingDataStore<Object, Object> dataStore = new SpyingDataStore<>(null, new AtomicLong());
        support.setDataStore(dataStore);
        support.setRepositoryMetadata(
                new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        final Person entity = new Person();
        entity.setId("k1");
        final Object saved = support.save(entity);
        assertThat(saved, Matchers.is(entity));
        assertThat(dataStore.getRequests(), hasSize(1));
        assertThat(dataStore.getRequests().get(0).getEntity(), Matchers.is(entity));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.SAVE));
        assertThat(dataStore.getRequests().get(0).getKey(), Matchers.is(entity.getId()));
    }

    @Test(expectedExceptions = DataStoreException.class)
    public void testPerformingInsertsWhenNoKeyGeneratorIsPresent() {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final DataStore<String, Person> dataStore = new MemoryDataStore<>(Person.class);
        support.setDataStore(dataStore);
        support.setRepositoryMetadata(
                new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        support.save(new Person());
    }

    @Test
    public void testPerformingInsertsWhenAKeyGeneratorIsPresent() {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final MemoryDataStore<String, Person> actualDataStore = new MemoryDataStore<>(Person.class);
        final SpyingDataStore<String, Person> dataStore = new SpyingDataStore<>(actualDataStore, new AtomicLong());
        support.setDataStore(dataStore);
        support.setRepositoryMetadata(
                new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        support.setKeyGenerator(new UUIDKeyGenerator());
        final Person entity = new Person();
        final Object saved = support.save(entity);
        assertThat(saved, Matchers.is(entity));
        assertThat(dataStore.getRequests(), hasSize(1));
        assertThat(dataStore.getRequests().get(0).getEntity(), Matchers.is(entity));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.SAVE));
        assertThat(dataStore.getRequests().get(0).getKey(), is(notNullValue()));
    }

    @Test
    public void testInsertingMultipleEntities() {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final MemoryDataStore<String, Person> actualDataStore = new MemoryDataStore<>(Person.class);
        final SpyingDataStore<String, Person> dataStore = new SpyingDataStore<>(actualDataStore, new AtomicLong());
        support.setDataStore(dataStore);
        support.setRepositoryMetadata(
                new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        support.setKeyGenerator(new UUIDKeyGenerator());

        final Person first = new Person();
        final Person second = new Person();
        final Person third = new Person();

        support.insert(Arrays.asList(first, second, third));

        assertThat(dataStore.getRequests(), hasSize(3));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.SAVE));
        assertThat(dataStore.getRequests().get(0).getEntity(), is(first));
        assertThat(dataStore.getRequests().get(1).getOperation(), is(Operation.SAVE));
        assertThat(dataStore.getRequests().get(1).getEntity(), is(second));
        assertThat(dataStore.getRequests().get(2).getOperation(), is(Operation.SAVE));
        assertThat(dataStore.getRequests().get(2).getEntity(), is(third));
    }
}