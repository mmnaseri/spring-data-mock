package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.KeyGenerationStrategy;
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
import java.util.stream.StreamSupport;

import static com.mmnaseri.utils.spring.data.utils.TestUtils.iterableToList;
import static java.util.stream.Collectors.toList;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/11/16, 10:15 AM)
 */
public class DefaultCrudRepositoryWithAllUnamangedKeyGenerationStrategyTest {

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
        repository.setKeyGenerationStrategy(KeyGenerationStrategy.ALL_UNMANAGED);
    }

    @Test
    public void testSave() {
        final Person managedPerson = new Person().setId("4");
        dataStore.save(managedPerson.getId(), managedPerson);
        final List<Person> entities = Arrays.asList(
                new Person().setId("1"),
                new Person().setId("2"),
                new Person().setId("3"),
                managedPerson
        );
        final Iterable<Object> inserted = repository.saveAll(entities);
        assertThat(inserted, is(notNullValue()));
        final List<Object> insertedList = StreamSupport.stream(inserted.spliterator(), /* parallel= */ false).collect(toList());
        assertThat(insertedList, hasSize(4));
        for (Object item : insertedList) {
            assertThat(item, is(instanceOf(Person.class)));
            assertThat((Person) item, isIn(entities));
            assertThat(((Person) item).getId(), is(notNullValue()));
            if (managedPerson == item) {
                assertThat(((Person) item).getId(), is("4"));
            } else {
                assertThat(((Person) item).getId(), not(isOneOf("1", "2", "3")));
            }
        }
        assertThat(dataStore.retrieveAll(), hasSize(entities.size()));
        final Iterable<Object> updated = repository.saveAll(entities);
        assertThat(updated, is(notNullValue()));
        final List<Object> updatedList = StreamSupport.stream(updated.spliterator(), /* parallel= */ false).collect(toList());
        assertThat(updatedList, hasSize(4));
        for (Object item : updatedList) {
            assertThat(item, is(instanceOf(Person.class)));
            assertThat((Person) item, isIn(entities));
        }
        assertThat(dataStore.retrieveAll(), hasSize(entities.size()));
    }

}
