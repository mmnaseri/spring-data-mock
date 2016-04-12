package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.domain.repository.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.error.DataStoreException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import com.mmnaseri.utils.spring.data.store.mock.Operation;
import com.mmnaseri.utils.spring.data.store.mock.SpyingDataStore;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/11/16, 10:15 AM)
 */
public class CrudRepositorySupportTest {

    @Test
    public void testIntegrity() throws Exception {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final MemoryDataStore<Serializable, Object> dataStore = new MemoryDataStore<>(Object.class);
        support.setDataStore(dataStore);
        final ImmutableRepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id");
        support.setRepositoryMetadata(repositoryMetadata);
        final UUIDKeyGenerator keyGenerator = new UUIDKeyGenerator();
        support.setKeyGenerator(keyGenerator);
        assertThat(support.getDataStore(), is(dataStore));
        assertThat(support.getKeyGenerator(), is(keyGenerator));
        assertThat(support.getRepositoryMetadata(), is(repositoryMetadata));
    }

    @Test
    public void testPerformingUpdates() throws Exception {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final SpyingDataStore<Serializable, Object> dataStore = new SpyingDataStore<>(null, new AtomicLong());
        support.setDataStore(dataStore);
        support.setRepositoryMetadata(new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        final Person entity = new Person();
        entity.setId("k1");
        final Object saved = support.save(entity);
        assertThat(saved, is(entity));
        assertThat(dataStore.getRequests(), hasSize(1));
        assertThat(dataStore.getRequests().get(0).getEntity(), is(entity));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.SAVE));
        assertThat(dataStore.getRequests().get(0).getKey(), is(entity.getId()));
    }

    /**
     * This needs to have the root cause logged. See #29
     * @throws Exception
     */
    @Test(expectedExceptions = DataStoreException.class)
    public void testPerformingInsertsWhenNoKeyGeneratorIsPresent() throws Exception {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final DataStore<String, Person> dataStore = new MemoryDataStore<>(Person.class);
        support.setDataStore(dataStore);
        support.setRepositoryMetadata(new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        support.save(new Person());
    }

    @Test
    public void testPerformingInsertsWhenAKeyGeneratorIsPresent() throws Exception {
        final CrudRepositorySupport support = new CrudRepositorySupport();
        final SpyingDataStore<String, Person> dataStore = new SpyingDataStore<>(new MemoryDataStore<>(Person.class), new AtomicLong());
        support.setDataStore(dataStore);
        support.setRepositoryMetadata(new ImmutableRepositoryMetadata(String.class, Person.class, SimplePersonRepository.class, "id"));
        support.setKeyGenerator(new UUIDKeyGenerator());
        final Person entity = new Person();
        final Object saved = support.save(entity);
        assertThat(saved, is(entity));
        assertThat(dataStore.getRequests(), hasSize(1));
        assertThat(dataStore.getRequests().get(0).getEntity(), is(entity));
        assertThat(dataStore.getRequests().get(0).getOperation(), is(Operation.SAVE));
        assertThat(dataStore.getRequests().get(0).getKey(), is(notNullValue()));
    }

}