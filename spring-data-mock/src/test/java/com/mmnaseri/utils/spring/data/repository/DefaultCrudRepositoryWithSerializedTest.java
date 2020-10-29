package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.sample.models.PersonSerializable;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonSerializableRepository;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/11/16, 10:15 AM)
 */
public class DefaultCrudRepositoryWithSerializedTest {

    private DefaultCrudRepository repository;
    private DataStore<String, PersonSerializable> dataStore;

    @BeforeMethod
    public void setUp() {
        dataStore = new MemoryDataStore<>(PersonSerializable.class);
        repository = new DefaultCrudRepository();
        repository.setRepositoryMetadata(new ImmutableRepositoryMetadata(String.class, PersonSerializable.class,
                                                                         SimplePersonSerializableRepository.class,
                                                                         "id"));
        repository.setDataStore(dataStore);
        repository.setKeyGenerator(new UUIDKeyGenerator());
    }


    @Test
    public void testDeleteByEntityWhenEntityHasKey() {
        final PersonSerializable original = new PersonSerializable();
        dataStore.save("1", original);
        assertThat(dataStore.hasKey("1"), is(true));
        final PersonSerializable personToDelete = new PersonSerializable();
        personToDelete.setId("1");
        final Object deleted = repository.delete(personToDelete);
        assertThat(dataStore.hasKey("1"), is(false));
        assertThat(deleted, is(notNullValue()));
        assertThat(deleted, Matchers.is(original));
    }

}