package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.error.DataFunctionException;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.sample.mocks.Operation;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingDataStore;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.*;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DeleteDataFunctionTest {

    private QueryDescriptor query;
    private DeleteDataFunction function;
    private SpyingDataStore<String, Person> dataStore;

    @BeforeMethod
    public void setUp() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, null, "id");
        final MemoryDataStore<String, Person> delegate = new MemoryDataStore<>(Person.class);
        query = new DefaultQueryDescriptor(false, null, 0, null, null, null, null, repositoryMetadata);
        function = new DeleteDataFunction();
        dataStore = new SpyingDataStore<>(delegate, new AtomicLong());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = "Data store .*")
    public void testPassingNullDataStore() throws Exception {
        function.apply(null, query, null, new LinkedList<>());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = "Query .*")
    public void testPassingNullQuery() throws Exception {
        function.apply(dataStore, null, null, new LinkedList<Person>());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = "Selection .*")
    public void testPassingNullSelection() throws Exception {
        function.apply(dataStore, query, null, null);
    }

    @Test(expectedExceptions = DataFunctionException.class, expectedExceptionsMessageRegExp = "Failed to read .*")
    public void testWrongIdentifierProperty() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, null, "xyz");
        final QueryDescriptor descriptor = new DefaultQueryDescriptor(false, null, 0, null, null, null, null, repositoryMetadata);
        function.apply(dataStore, descriptor, null, Collections.singletonList(new Person()));
    }

    @Test(expectedExceptions = DataFunctionException.class, expectedExceptionsMessageRegExp = "Cannot delete an entity without the key property being set: id")
    public void testNullIdentifierValue() throws Exception {
        function.apply(dataStore, query, null, Collections.singletonList(new Person()));
    }

    @Test
    public void testThatItSendsRequestsToTheUnderlyingDataStore() throws Exception {
        final List<Person> selection = new ArrayList<>();
        selection.add(new Person().setId("1"));
        selection.add(new Person().setId("2"));
        function.apply(dataStore, query, null, selection);
        assertThat(dataStore.getRequests(), hasSize(2));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.DELETE));
        assertThat(dataStore.getRequests().get(0).getKey(), Matchers.<Serializable>is(selection.get(0).getId()));
        assertThat(dataStore.getRequests().get(1).getOperation(), is(Operation.DELETE));
        assertThat(dataStore.getRequests().get(1).getKey(), Matchers.<Serializable>is(selection.get(1).getId()));
    }

    @Test
    public void testThatItDeletesTheExactSetOfItemsSpecified() throws Exception {
        final List<Person> entities = new ArrayList<>();
        entities.add(new Person().setId("1"));
        entities.add(new Person().setId("2"));
        entities.add(new Person().setId("3"));
        entities.add(new Person().setId("4"));
        for (Person person : entities) {
            dataStore.save(person.getId(), person);
        }
        dataStore.reset();
        final List<Person> selection = entities.subList(1, 3);
        function.apply(dataStore, query, null, selection);
        assertThat(dataStore.getRequests(), hasSize(2));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.DELETE));
        assertThat(dataStore.getRequests().get(0).getKey(), Matchers.<Serializable>is(entities.get(1).getId()));
        assertThat(dataStore.getRequests().get(1).getOperation(), is(Operation.DELETE));
        assertThat(dataStore.getRequests().get(1).getKey(), Matchers.<Serializable>is(entities.get(2).getId()));
        assertThat(dataStore.hasKey(entities.get(0).getId()), is(true));
        assertThat(dataStore.hasKey(entities.get(1).getId()), is(false));
        assertThat(dataStore.hasKey(entities.get(2).getId()), is(false));
        assertThat(dataStore.hasKey(entities.get(3).getId()), is(true));
    }

    @Test
    public void testThatItReturnsTheDeletedItems() throws Exception {
        final List<Person> entities = new ArrayList<>();
        entities.add(new Person().setId("1"));
        entities.add(new Person().setId("2"));
        entities.add(new Person().setId("3"));
        entities.add(new Person().setId("4"));
        for (Person person : entities) {
            dataStore.save(person.getId(), person);
        }
        dataStore.reset();
        final List<Person> selection = new ArrayList<>(entities.subList(1, 3));
        selection.add(new Person().setId("5"));
        final List<Person> deleted = function.apply(dataStore, query, null, selection);
        assertThat(dataStore.getRequests(), hasSize(3));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.DELETE));
        assertThat(dataStore.getRequests().get(0).getKey(), Matchers.<Serializable>is(entities.get(1).getId()));
        assertThat(dataStore.getRequests().get(1).getOperation(), is(Operation.DELETE));
        assertThat(dataStore.getRequests().get(1).getKey(), Matchers.<Serializable>is(entities.get(2).getId()));
        assertThat(dataStore.getRequests().get(2).getOperation(), is(Operation.DELETE));
        assertThat(dataStore.getRequests().get(2).getKey(), Matchers.<Serializable>is(selection.get(2).getId()));
        assertThat(deleted, hasSize(2));
        final List<String> deletedIds = new ArrayList<>(Arrays.asList(entities.get(1).getId(), entities.get(2).getId()));
        for (Person person : deleted) {
            assertThat(person.getId(), isIn(deletedIds));
            deletedIds.remove(person.getId());
        }
        assertThat(deletedIds, is(Collections.<String>emptyList()));
    }

}