package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.domain.repository.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/11/16, 10:15 AM)
 */
public class DefaultCrudRepositoryTest {

    private DefaultCrudRepository repository;
    private DataStore<String, Person> dataStore;

    @BeforeMethod
    public void setUp() throws Exception {
        dataStore = new MemoryDataStore<>(Person.class);
        repository = new DefaultCrudRepository();
        repository.setRepositoryMetadata(new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        repository.setDataStore(dataStore);
        repository.setKeyGenerator(new UUIDKeyGenerator());
    }

    @Test
    public void testSave() throws Exception {
        final List<Person> entities = Arrays.asList(new Person(), new Person(), new Person());
        final Iterable<Object> inserted = repository.save(entities);
        assertThat(inserted, is(notNullValue()));
        final List<Object> insertedList = iterableToList(inserted);
        assertThat(insertedList, hasSize(3));
        for (Object item : insertedList) {
            assertThat(item, is(instanceOf(Person.class)));
            assertThat((Person) item, isIn(entities));
            assertThat(((Person) item).getId(), is(notNullValue()));
        }
        assertThat(dataStore.retrieveAll(), hasSize(entities.size()));
        final Iterable<Object> updated = repository.save(entities);
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
    public void testFindOne() throws Exception {
        final String key = "1234";
        assertThat(repository.findOne(key), is(nullValue()));
        final Person person = new Person();
        dataStore.save("1234", person);
        assertThat(repository.findOne(key), Matchers.<Object>is(person));
    }

    @Test
    public void testExists() throws Exception {
        final String key = "1234";
        assertThat(repository.exists(key), is(false));
        dataStore.save("1234", new Person());
        assertThat(repository.exists(key), is(true));
    }

    @Test
    public void testFindAll() throws Exception {
        dataStore.save("1", new Person());
        dataStore.save("2", new Person());
        dataStore.save("3", new Person());
        dataStore.save("4", new Person());
        final List<?> list = iterableToList(repository.findAll());
        final Collection<Person> people = dataStore.retrieveAll();
        assertThat(list, hasSize(people.size()));
        for (Object item : list) {
            //noinspection unchecked
            assertThat(item, isIn((Collection) people));
        }
    }

    @Test
    public void testFindAllById() throws Exception {
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
        final List<?> list = iterableToList(repository.findAll(request));
        assertThat(list, hasSize(existingIds.size()));
        for (Object found : list) {
            assertThat(found, is(instanceOf(Person.class)));
            assertThat((Person) found, isIn(expected));
        }
    }

    @Test
    public void testDeleteByKey() throws Exception {
        final Object deleted = repository.delete("1");
        assertThat(deleted, is(nullValue()));
    }

    @Test
    public void testDeleteByEntityWhenEntityHasKey() throws Exception {
        final Person original = new Person();
        dataStore.save("1", original);
        assertThat(dataStore.hasKey("1"), is(true));
        final Object deleted = repository.delete(new Person().setId("1"));
        assertThat(dataStore.hasKey("1"), is(false));
        assertThat(deleted, is(notNullValue()));
        assertThat(deleted, Matchers.<Object>is(original));
    }

    @Test(expectedExceptions = EntityMissingKeyException.class)
    public void testDeleteByEntityWhenEntityHasNoKey() throws Exception {
        repository.delete(new Person());
    }

    @Test
    public void testDeleteMany() throws Exception {
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
    public void testDeleteAll() throws Exception {
        dataStore.save("1", new Person());
        dataStore.save("2", new Person());
        dataStore.save("3", new Person());
        dataStore.save("4", new Person());
        assertThat(dataStore.keys(), hasSize(4));
        repository.deleteAll();
        assertThat(dataStore.keys(), is(empty()));
    }

    private <E> List<E> iterableToList(Iterable<E> iterable) {
        final List<E> list = new ArrayList<>();
        for (E item : iterable) {
            list.add(item);
        }
        return list;
    }

}