package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import com.mmnaseri.utils.spring.data.utils.TestUtils;
import com.querydsl.core.NonUniqueResultException;
import com.querydsl.core.types.dsl.BooleanExpression;
import org.hamcrest.Matchers;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.querydsl.core.alias.Alias.$;
import static com.querydsl.core.alias.Alias.alias;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/28/16)
 */
@Ignore
public class DefaultQueryDslPredicateExecutorTest {

    private MemoryDataStore<Object, Person> dataStore;
    private DefaultQueryDslPredicateExecutor executor;

    @BeforeMethod
    public void setUp() throws Exception {
        dataStore = new MemoryDataStore<>(Person.class);
        executor = new DefaultQueryDslPredicateExecutor();
        executor.setDataStore(dataStore);
    }

    @Test
    public void testFindOne() throws Exception {
        final Person saved = new Person().setId("1").setAge(10);
        dataStore.save("1", saved);
        dataStore.save("2", new Person().setId("2"));
        final Person person = alias(Person.class);
        final BooleanExpression predicate = $(person.getId()).eq("1");
        final Object one = executor.findOne(predicate);
        assertThat(one, is(notNullValue()));
        assertThat(one, Matchers.<Object>is(saved));
    }

    @Test
    public void testFindOneWhenNoneExists() throws Exception {
        final Person person = alias(Person.class);
        final BooleanExpression predicate = $(person.getId()).eq("1");
        final Object one = executor.findOne(predicate);
        assertThat(one, is(nullValue()));
    }

    @Test(expectedExceptions = NonUniqueResultException.class)
    public void testFindOneWhenMoreThanOneMatches() throws Exception {
        dataStore.save("1", new Person().setId("1").setFirstName("Milad").setLastName("Naseri"));
        dataStore.save("2", new Person().setId("2").setFirstName("Hassan").setLastName("Naseri"));
        final Person person = alias(Person.class);
        final BooleanExpression predicate = $(person.getLastName()).eq("Naseri");
        final Object one = executor.findOne(predicate);
        assertThat(one, is(nullValue()));
    }

    @Test
    public void testFindAllUsingAPredicate() throws Exception {
        dataStore.save("1", new Person().setId("1").setAge(10));
        dataStore.save("2", new Person().setId("2").setAge(15));
        dataStore.save("3", new Person().setId("3").setAge(20));
        dataStore.save("4", new Person().setId("4").setAge(25));
        dataStore.save("5", new Person().setId("5").setAge(30));
        final Person person = alias(Person.class);
        final Iterable result = executor.findAll($(person.getAge()).gt(20).or($(person.getAge()).eq(20)));
        assertThat(result, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(result);
        assertThat(list, hasSize(3));
        final List<String> ids = new ArrayList<>(Arrays.asList("3", "4", "5"));
        for (Object obj : list) {
            assertThat(obj, is(instanceOf(Person.class)));
            final Person loaded = (Person) obj;
            assertThat(loaded.getId(), isIn(ids));
            ids.remove(loaded.getId());
        }
        assertThat(ids, is(Matchers.<String>empty()));
    }

    @Test
    public void testFindAllWithSort() throws Exception {
        dataStore.save("1", new Person().setId("1").setAge(30));
        dataStore.save("2", new Person().setId("2").setAge(25));
        dataStore.save("3", new Person().setId("3").setAge(20));
        dataStore.save("4", new Person().setId("4").setAge(15));
        dataStore.save("5", new Person().setId("5").setAge(10));
        final Person person = alias(Person.class);
        final Iterable result = executor.findAll($(person.getAge()).gt(20).or($(person.getAge()).eq(20)),
                                                 Sort.by(Sort.Direction.ASC, "age"));
        assertThat(result, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(result);
        assertThat(list, hasSize(3));
        assertThat(list.get(0), Matchers.<Object>is(dataStore.retrieve("3")));
        assertThat(list.get(1), Matchers.<Object>is(dataStore.retrieve("2")));
        assertThat(list.get(2), Matchers.<Object>is(dataStore.retrieve("1")));
    }

    @Test
    public void testFindAllWithPagingAndSorting() throws Exception {
        dataStore.save("1", new Person().setId("1").setAge(30));
        dataStore.save("2", new Person().setId("2").setAge(25));
        dataStore.save("3", new Person().setId("3").setAge(20));
        dataStore.save("4", new Person().setId("4").setAge(15));
        dataStore.save("5", new Person().setId("5").setAge(10));
        final Person person = alias(Person.class);
        final Iterable result = executor.findAll($(person.getAge()).gt(20).or($(person.getAge()).eq(20)),
                                                 PageRequest.of(0, 2, Sort.by(Sort.Direction.ASC, "age")));
        assertThat(result, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(result);
        assertThat(list, hasSize(2));
        assertThat(list.get(0), Matchers.<Object>is(dataStore.retrieve("3")));
        assertThat(list.get(1), Matchers.<Object>is(dataStore.retrieve("2")));
    }

    @Test
    public void testFindAllWithPaging() throws Exception {
        dataStore.save("1", new Person().setId("1").setAge(30));
        dataStore.save("2", new Person().setId("2").setAge(25));
        dataStore.save("3", new Person().setId("3").setAge(20));
        dataStore.save("4", new Person().setId("4").setAge(15));
        dataStore.save("5", new Person().setId("5").setAge(10));
        final Person person = alias(Person.class);
        final Iterable result = executor.findAll($(person.getAge()).gt(20).or($(person.getAge()).eq(20)),
                                                 PageRequest.of(0, 2));
        assertThat(result, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(result);
        assertThat(list, hasSize(2));
        for (Object obj : list) {
            assertThat(obj, is(instanceOf(Person.class)));
            final Person loaded = (Person) obj;
            assertThat(loaded, is(dataStore.retrieve(loaded.getId())));
        }
    }

    @Test
    public void testCount() throws Exception {
        dataStore.save("1", new Person().setId("1").setAge(30));
        dataStore.save("2", new Person().setId("2").setAge(25));
        dataStore.save("3", new Person().setId("3").setAge(20));
        dataStore.save("4", new Person().setId("4").setAge(15));
        dataStore.save("5", new Person().setId("5").setAge(10));
        final Person person = alias(Person.class);
        final long count = executor.count($(person.getAge()).gt(20).or($(person.getAge()).eq(20)));
        assertThat(count, is((long) 3));
    }

    @Test
    public void testExists() throws Exception {
        dataStore.save("1", new Person().setId("1").setAge(30));
        dataStore.save("2", new Person().setId("2").setAge(25));
        dataStore.save("3", new Person().setId("3").setAge(20));
        dataStore.save("4", new Person().setId("4").setAge(15));
        dataStore.save("5", new Person().setId("5").setAge(10));
        final Person person = alias(Person.class);
        assertThat(executor.exists($(person.getAge()).gt(20).or($(person.getAge()).eq(20))), is(true));
        assertThat(executor.exists($(person.getAge()).lt(20).or($(person.getAge()).eq(20))), is(true));
        assertThat(executor.exists($(person.getAge()).lt(10).or($(person.getAge()).eq(10))), is(true));
        assertThat(executor.exists($(person.getAge()).lt(5).or($(person.getAge()).eq(5))), is(false));
    }

    @Test
    public void testFindAllWithExplicitOrdering() throws Exception {
        dataStore.save("1", new Person().setId("1").setAge(30));
        dataStore.save("2", new Person().setId("2").setAge(25));
        dataStore.save("3", new Person().setId("3").setAge(20));
        dataStore.save("4", new Person().setId("4").setAge(15));
        dataStore.save("5", new Person().setId("5").setAge(10));
        final Person person = alias(Person.class);
        final Iterable all = executor.findAll($(person.getAge()).asc());
        assertThat(all, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(all);
        assertThat(list, hasSize(5));
        assertThat(list.get(0), Matchers.<Object>is(dataStore.retrieve("5")));
        assertThat(list.get(1), Matchers.<Object>is(dataStore.retrieve("4")));
        assertThat(list.get(2), Matchers.<Object>is(dataStore.retrieve("3")));
        assertThat(list.get(3), Matchers.<Object>is(dataStore.retrieve("2")));
        assertThat(list.get(4), Matchers.<Object>is(dataStore.retrieve("1")));
    }

    @Test
    public void testFindAllWithPredicateAndExplicitOrdering() throws Exception {
        dataStore.save("1", new Person().setId("1").setAge(30));
        dataStore.save("2", new Person().setId("2").setAge(25));
        dataStore.save("3", new Person().setId("3").setAge(20));
        dataStore.save("4", new Person().setId("4").setAge(15));
        dataStore.save("5", new Person().setId("5").setAge(10));
        final Person person = alias(Person.class);
        final Iterable all = executor.findAll($(person.getAge()).gt(20), $(person.getAge()).asc());
        assertThat(all, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(all);
        assertThat(list, hasSize(2));
        assertThat(list.get(0), Matchers.<Object>is(dataStore.retrieve("2")));
        assertThat(list.get(1), Matchers.<Object>is(dataStore.retrieve("1")));
    }

}