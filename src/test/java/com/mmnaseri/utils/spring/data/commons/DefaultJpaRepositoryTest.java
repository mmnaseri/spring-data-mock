package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.domain.repository.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import com.mmnaseri.utils.spring.data.store.mock.Operation;
import com.mmnaseri.utils.spring.data.store.mock.SpyingDataStore;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/11/16, 1:16 PM)
 */
public class DefaultJpaRepositoryTest {

    private RepositoryMetadata repositoryMetadata;
    private DefaultJpaRepository repository;
    private DataStore<String, Person> dataStore;

    @BeforeMethod
    public void setUp() throws Exception {
        repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id");
        dataStore = new MemoryDataStore<>(Person.class);
        repository = new DefaultJpaRepository();
        repository.setDataStore(dataStore);
        repository.setRepositoryMetadata(repositoryMetadata);
        repository.setKeyGenerator(new UUIDKeyGenerator());
    }

    @Test
    public void testFlushing() throws Exception {
        final DefaultJpaRepository repository = new DefaultJpaRepository();
        repository.setKeyGenerator(new UUIDKeyGenerator());
        repository.setRepositoryMetadata(repositoryMetadata);
        final SpyingDataStore<Serializable, Object> dataStore = new SpyingDataStore<>(null, new AtomicLong());
        repository.setDataStore(dataStore);
        repository.flush();
        assertThat(dataStore.getRequests(), hasSize(1));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.FLUSH));
    }

    @Test
    public void testDeleteInBatch() throws Exception {
        dataStore.save("1", new Person());
        dataStore.save("2", new Person());
        dataStore.save("3", new Person());
        dataStore.save("4", new Person());
        final List<String> existingIds = Arrays.asList("2", "3");
        final List<String> missingIds = Arrays.asList("5", "6");
        final List<Person> request = new ArrayList<>();
        for (String id : existingIds) {
            request.add(new Person().setId(id));
        }
        for (String id : missingIds) {
            request.add(new Person().setId(id));
        }
        final List<Person> expected = new ArrayList<>();
        for (String id : existingIds) {
            expected.add(dataStore.retrieve(id));
        }
        for (String existingId : existingIds) {
            assertThat(dataStore.hasKey(existingId), is(true));
        }
        final List<?> deleted = iterableToList(repository.deleteInBatch(request));
        assertThat(deleted, hasSize(existingIds.size()));
        for (Object item : deleted) {
            assertThat(item, is(instanceOf(Person.class)));
            assertThat((Person) item, isIn(expected));
        }
    }

    @Test(expectedExceptions = EntityMissingKeyException.class)
    public void testDeleteInBatchWhenIdIsNull() throws Exception {
        repository.deleteInBatch(Collections.singleton(new Person()));
    }

    @Test
    public void testDeleteAllInBatch() throws Exception {
        dataStore.save("1", new Person());
        dataStore.save("2", new Person());
        dataStore.save("3", new Person());
        dataStore.save("4", new Person());
        assertThat(dataStore.keys(), hasSize(4));
        repository.deleteAllInBatch();
        assertThat(dataStore.keys(), is(empty()));
    }

    @Test
    public void testGetOne() throws Exception {
        final String key = "1234";
        assertThat(repository.getOne(key), is(nullValue()));
        final Person person = new Person();
        dataStore.save("1234", person);
        assertThat(repository.getOne(key), Matchers.<Object>is(person));
    }

    @Test
    public void testSaveAndFlush() throws Exception {
        final DefaultJpaRepository repository = new DefaultJpaRepository();
        repository.setKeyGenerator(new UUIDKeyGenerator());
        repository.setRepositoryMetadata(repositoryMetadata);
        final SpyingDataStore<String, Person> dataStore = new SpyingDataStore<>(this.dataStore, new AtomicLong());
        repository.setDataStore(dataStore);
        final Person entity = new Person().setId("1");
        repository.saveAndFlush(entity);
        assertThat(dataStore.getRequests().get(dataStore.getRequests().size() - 1).getOperation(), is(Operation.FLUSH));
        assertThat(dataStore.retrieve("1"), is(entity));
    }

    private <E> List<E> iterableToList(Iterable<E> iterable) {
        final List<E> list = new ArrayList<>();
        for (E item : iterable) {
            list.add(item);
        }
        return list;
    }

}