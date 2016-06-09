package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultOperatorContext;
import com.mmnaseri.utils.spring.data.domain.impl.DefaultRepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.ImmutableRepositoryConfiguration;
import com.mmnaseri.utils.spring.data.sample.models.Address;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.models.State;
import com.mmnaseri.utils.spring.data.sample.models.Zip;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import com.mmnaseri.utils.spring.data.utils.TestUtils;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/8/16, 12:12 PM)
 */
public class DefaultQueryByExampleExecutorTest {

    private DefaultQueryByExampleExecutor executor;
    private DataStore<Serializable, Person> dataStore;

    @BeforeMethod
    public void setUp() throws Exception {
        dataStore = new MemoryDataStore<>(Person.class);
        dataStore.save("1",
                new Person()
                        .setId("1")
                        .setFirstName("Milad")
                        .setLastName("Naseri")
                        .setAge(28)
                        .setAddressZip(new Zip())
                        .setAddress(
                                new Address()
                                        .setCity("Tehran")
                                        .setState(
                                                new State()
                                                        .setName("Teheran")
                                        )
                        )
        );
        dataStore.save("2",
                new Person()
                        .setId("2")
                        .setFirstName("Zohreh")
                        .setLastName("Sadeghi")
                        .setAge(26)
                        .setAddressZip(new Zip())
                        .setAddress(
                                new Address()
                                        .setCity("Edmonds")
                                        .setState(
                                                new State()
                                                        .setName("WA")
                                        )
                        )
        );
        dataStore.save("3",
                new Person()
                        .setId("3")
                        .setFirstName("Ramin")
                        .setLastName("Farhanian")
                        .setAge(40)
                        .setAddressZip(null)
                        .setAddress(
                                new Address()
                                        .setCity("Kirkland")
                                        .setState(
                                                new State()
                                                        .setName("WA")
                                        )
                        )
        );
        dataStore.save("4",
                new Person()
                        .setId("4")
                        .setFirstName("Niloufar")
                        .setLastName("Poursultani")
                        .setAge(53)
                        .setAddressZip(new Zip())
                        .setAddress(
                                new Address()
                                        .setCity("Tehran")
                                        .setState(
                                                new State()
                                                        .setName("Teheran")
                                        )
                        )
        );
        executor = new DefaultQueryByExampleExecutor();
        executor.setDataStore(dataStore);
        final RepositoryMetadata metadata = new DefaultRepositoryMetadataResolver().resolve(SimplePersonRepository.class);
        executor.setRepositoryMetadata(metadata);
        executor.setRepositoryConfiguration(new ImmutableRepositoryConfiguration(metadata, null, null));
        final DefaultRepositoryFactoryConfiguration configuration = new DefaultRepositoryFactoryConfiguration();
        configuration.setDescriptionExtractor(new MethodQueryDescriptionExtractor(new DefaultOperatorContext()));
        executor.setRepositoryFactoryConfiguration(configuration);
    }

    @Test
    public void testFindOneWhenThereIsNoMatch() throws Exception {
        final Object result = executor.findOne(Example.of(new Person().setFirstName("Gigili").setLastName("Magooli")));
        assertThat(result, is(nullValue()));
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testFindOneWhenThereIsMoreThanOneMatch() throws Exception {
        executor.findOne(Example.of(new Person().setAddress(new Address().setCity("Tehran"))));
    }

    @Test
    public void testFindOne() throws Exception {
        final Object found = executor.findOne(Example.of(new Person().setAddress(new Address().setCity("Tehran")).setFirstName("Milad")));
        assertThat(found, is(notNullValue()));
        assertThat(found, is((Object) dataStore.retrieve("1")));
    }

    @Test
    public void testFindByExampleUsingEndingWithMatcher() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("ran")));
        final ExampleMatcher matching = ExampleMatcher.matching().withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.endsWith()).withIgnoreCase();
        final Example<Person> example = Example.of(probe, matching);
        final Iterable found = executor.findAll(example);
        assertThat(found, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(found);
        assertThat(list, hasSize(2));
    }

    @Test
    public void testFindByExampleUsingStartingWithMatcher() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("Teh")));
        final ExampleMatcher matching = ExampleMatcher.matching().withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.startsWith()).withIgnoreCase();
        final Example<Person> example = Example.of(probe, matching);
        final Iterable found = executor.findAll(example);
        assertThat(found, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(found);
        assertThat(list, hasSize(2));
    }

    @Test
    public void testFindByExampleUsingContainingMatcher() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("ehe")));
        final ExampleMatcher matching = ExampleMatcher.matching().withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.contains()).withIgnoreCase();
        final Example<Person> example = Example.of(probe, matching);
        final Iterable found = executor.findAll(example);
        assertThat(found, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(found);
        assertThat(list, hasSize(2));
    }

    @Test
    public void testFindByExampleUsingRegexMatcher() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("(WA|Teheran)")));
        final ExampleMatcher matching = ExampleMatcher.matching().withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.regex()).withIgnoreCase();
        final Example<Person> example = Example.of(probe, matching);
        final Iterable found = executor.findAll(example);
        assertThat(found, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(found);
        assertThat(list, hasSize(4));
    }

    @Test
    public void testFindByExampleWithSorting() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("(WA|Teheran)")));
        final ExampleMatcher matching = ExampleMatcher.matching().withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.regex()).withIgnoreCase();
        final Example<Person> example = Example.of(probe, matching);
        final Iterable found = executor.findAll(example, new Sort(Sort.Direction.ASC, "firstName", "lastName", "address.state.name"));
        assertThat(found, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(found);
        assertThat(list, hasSize(4));
        assertThat(list.get(0), is((Object) dataStore.retrieve("1")));
        assertThat(list.get(1), is((Object) dataStore.retrieve("4")));
        assertThat(list.get(2), is((Object) dataStore.retrieve("3")));
        assertThat(list.get(3), is((Object) dataStore.retrieve("2")));
    }

    @Test
    public void testFindByExampleWithSortingAndPaging() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("Teheran")));
        final ExampleMatcher matching = ExampleMatcher.matching().withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.regex()).withIgnoreCase().withIgnorePaths("address.state.name");
        final Example<Person> example = Example.of(probe, matching);
        final Iterable found = executor.findAll(example, new PageRequest(2, 1, new Sort(Sort.Direction.ASC, "firstName", "lastName", "address.state.name")));
        assertThat(found, is(notNullValue()));
        final List<?> list = TestUtils.iterableToList(found);
        assertThat(list, hasSize(1));
        assertThat(list.get(0), is((Object) dataStore.retrieve("3")));
    }

    @Test
    public void testCount() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("ehe")));
        final ExampleMatcher matching = ExampleMatcher.matching().withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.contains()).withIgnoreCase();
        final Example<Person> example = Example.of(probe, matching);
        final long count = executor.count(example);
        assertThat(count, is(2L));
    }

    @Test
    public void testExists() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("ehe")));
        final ExampleMatcher matching = ExampleMatcher.matching()
                .withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.contains());
        final Example<Person> example = Example.of(probe, matching);
        final boolean exists = executor.exists(example);
        assertThat(exists, is(true));
    }

    @Test
    public void testWithNullsKept() throws Exception {
        final Person probe = new Person().setAddress(new Address().setState(new State().setName("ehe")));
        final ExampleMatcher matching = ExampleMatcher.matching()
                .withMatcher("address.state.name", ExampleMatcher.GenericPropertyMatchers.contains())
                .withIncludeNullValues();
        final Example<Person> example = Example.of(probe, matching);
        final boolean exists = executor.exists(example);
        assertThat(exists, is(false));
    }

}