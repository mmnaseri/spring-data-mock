package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static com.mmnaseri.utils.spring.data.utils.TestUtils.iterableToList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/11/16, 10:15 AM)
 */
public class DefaultCrudRepositoryTest {

    private DefaultCrudRepository repository;
    private DataStore<String, Person> dataStore;

    @BeforeMethod
    public void setUp() {
        dataStore = new MemoryDataStore<>(Person.class);
        repository = new DefaultCrudRepository();
        repository.setRepositoryMetadata(
                new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        repository.setDataStore(dataStore);
        repository.setKeyGenerator(new UUIDKeyGenerator());
    }

    @Test
    public void testSave() {
        final List<Person> entities = Arrays.asList(new Person(), new Person(), new Person());
        final Iterable<Object> inserted = repository.saveAll(entities);
        assertThat(inserted, is(notNullValue()));
        final List<Object> insertedList = iterableToList(inserted);
        assertThat(insertedList, hasSize(3));
        for (Object item : insertedList) {
            assertThat(item, is(instanceOf(Person.class)));
            assertThat((Person) item, isIn(entities));
            assertThat(((Person) item).getId(), is(notNullValue()));
        }
        assertThat(dataStore.retrieveAll(), hasSize(entities.size()));
        final Iterable<Object> updated = repository.saveAll(entities);
        assertThat(updated, is(notNullValue()));
        final List<Object> updatedList = iterableToList(updated);
        assertThat(updatedList, hasSize(3));
        for (Object item : updatedList) {
            assertThat(item, is(instanceOf(Person.class)));
            assertThat((Person) item, isIn(entities));
        }
        assertThat(dataStore.retrieveAll(), hasSize(entities.size()));
    }

    @Test
    public void testFindOne() {
        final String key = "1234";
        assertThat(repository.findById(key), is(Optional.empty()));
        final Person person = new Person();
        dataStore.save("1234", person);
        assertThat(repository.findById(key).isPresent(), is(true));
        assertThat(repository.findById(key).get(), is(person));
    }

    @Test
    public void testExists() {
        final String key = "1234";
        assertThat(repository.existsById(key), is(false));
        dataStore.save("1234", new Person());
        assertThat(repository.existsById(key), is(true));
    }

    @Test
    public void testFindAllById() {
        dataStore.save("1", new Person());
        dataStore.save("2", new Person());
        dataStore.save("3", new Person());
        dataStore.save("4", new Person());
        final List<String> existingIds = Arrays.asList("2", "3");
        final List<String> missingIds = Arrays.asList("5", "6");
        final List<String> request = new ArrayList<>();
        request.addAll(existingIds);
        request.addAll(missingIds);
        final List<Person> expected = new ArrayList<>();
        for (String id : existingIds) {
            expected.add(dataStore.retrieve(id));
        }
        final List<?> list = iterableToList(repository.findAllById(request));
        assertThat(list, hasSize(existingIds.size()));
        for (Object found : list) {
            assertThat(found, is(instanceOf(Person.class)));
            assertThat((Person) found, isIn(expected));
        }
    }

    @Test
    public void testDeleteByKey() {
        final Object deleted = repository.delete("1");
        assertThat(deleted, is(nullValue()));
    }

    @Test
    public void testDeleteByEntityWhenEntityHasKey() {
        final Person original = new Person();
        dataStore.save("1", original);
        assertThat(dataStore.hasKey("1"), is(true));
        final Object deleted = repository.deleteById(new Person().setId("1"));
        assertThat(dataStore.hasKey("1"), is(false));
        assertThat(deleted, is(notNullValue()));
        assertThat(deleted, is(original));
    }

    @Test(expectedExceptions = EntityMissingKeyException.class)
    public void testDeleteByEntityWhenEntityHasNoKey() {
        repository.deleteById(new Person());
    }

    @Test
    public void testDeleteMany() {
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
        final List<?> deleted = iterableToList(repository.delete(request));
        assertThat(deleted, hasSize(existingIds.size()));
        for (Object item : deleted) {
            assertThat(item, is(instanceOf(Person.class)));
            assertThat((Person) item, isIn(expected));
        }
    }

    @Test
    public void testDeleteAll() {
        dataStore.save("1", new Person());
        dataStore.save("2", new Person());
        dataStore.save("3", new Person());
        dataStore.save("4", new Person());
        assertThat(dataStore.keys(), hasSize(4));
        repository.deleteAll();
        assertThat(dataStore.keys(), is(empty()));
    }

}