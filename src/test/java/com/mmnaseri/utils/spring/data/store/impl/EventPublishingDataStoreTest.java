package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.error.CorruptDataException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreEvent;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListener;
import com.mmnaseri.utils.spring.data.store.DataStoreEventListenerContext;
import org.hamcrest.Matchers;
import org.springframework.data.repository.Repository;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
@SuppressWarnings("WeakerAccess")
public class EventPublishingDataStoreTest {

    private static AtomicLong counter = new AtomicLong(0);

    public interface PersonRepository extends Repository<Person, String> {}

    private static class EventTrigger {

        private final Long timestamp;
        private final DataStoreEvent event;

        private EventTrigger(Long timestamp, DataStoreEvent event) {
            this.timestamp = timestamp;
            this.event = event;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public DataStoreEvent getEvent() {
            return event;
        }
    }

    private static class SpyingListenerContext implements DataStoreEventListenerContext {

        private final List<EventTrigger> events = new ArrayList<>();

        @Override
        public <E extends DataStoreEvent> void register(DataStoreEventListener<E> listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public void trigger(DataStoreEvent event) {
            events.add(new EventTrigger(counter.incrementAndGet(), event));
        }

        public List<EventTrigger> getEvents() {
            return events;
        }

    }

    private static class DummyEvent implements DataStoreEvent {
        @Override
        public RepositoryMetadata getRepositoryMetadata() {
            return null;
        }

        @Override
        public DataStore<?, ?> getDataStore() {
            return null;
        }
    }

    private enum Operation {

        SAVE, DELETE

    }

    private static class OperationRequest {

        private final Long timestamp;
        private final Operation operation;
        private final String key;
        private final Person entity;

        private OperationRequest(Long timestamp, Operation operation, String key, Person entity) {
            this.timestamp = timestamp;
            this.operation = operation;
            this.key = key;
            this.entity = entity;
        }

        public Long getTimestamp() {
            return timestamp;
        }

        public Operation getOperation() {
            return operation;
        }

        public String getKey() {
            return key;
        }

        public Person getEntity() {
            return entity;
        }
    }

    private static class SpyingDataStore implements DataStore<String, Person> {

        private final DataStore<String, Person> delegate;
        private final List<OperationRequest> requests;

        private SpyingDataStore(DataStore<String, Person> delegate) {
            this.delegate = delegate;
            requests = new ArrayList<>();
        }

        @Override
        public boolean hasKey(String key) {
            return delegate.hasKey(key);
        }

        @Override
        public void save(String key, Person entity) {
            requests.add(new OperationRequest(counter.incrementAndGet(), Operation.SAVE, key, entity));
            delegate.save(key, entity);
        }

        @Override
        public void delete(String key) {
            requests.add(new OperationRequest(counter.incrementAndGet(), Operation.DELETE, key, null));
            delegate.delete(key);
        }

        @Override
        public Person retrieve(String key) {
            return delegate.retrieve(key);
        }

        @Override
        public Collection<Person> retrieveAll() {
            return delegate.retrieveAll();
        }

        @Override
        public Class<Person> getEntityType() {
            return delegate.getEntityType();
        }

        public List<OperationRequest> getRequests() {
            return requests;
        }

    }


    private DataStore<String, Person> delegate;
    private SpyingDataStore delegateSpy;
    private DataStore<String, Person> dataStore;
    private SpyingListenerContext listenerContext;

    @BeforeMethod
    public void setUp() throws Exception {
        final RepositoryMetadata repositoryMetadata = new ImmutableRepositoryMetadata(String.class, Person.class, PersonRepository.class, "id");
        listenerContext = new SpyingListenerContext();
        delegate = new MemoryDataStore<>(Person.class);
        delegateSpy = new SpyingDataStore(delegate);
        dataStore = new EventPublishingDataStore<>(delegateSpy, repositoryMetadata, listenerContext);
    }

    @Test
    public void testHasKeyDelegation() throws Exception {
        final String key = "1";
        delegate.save(key, new Person());
        assertThat(dataStore.hasKey(key), is(true));
    }

    @Test
    public void testRetrieveDelegation() throws Exception {
        final String key = "key";
        assertThat(dataStore.retrieve(key), is(delegate.retrieve(key)));
        delegate.save(key, new Person());
        assertThat(dataStore.retrieve(key), is(delegate.retrieve(key)));
    }

    @Test
    public void testRetrieveAllDelegation() throws Exception {
        final String k1 = "k1";
        final String k2 = "k2";
        assertThat(dataStore.retrieveAll(), is(delegate.retrieveAll()));
        delegate.save(k1, new Person());
        delegate.save(k2, new Person());
        assertThat(dataStore.retrieveAll(), is(delegate.retrieveAll()));
    }

    @Test
    public void testEntityTypeDelegation() throws Exception {
        assertThat(dataStore.getEntityType(), Matchers.<Class<Person>>is(delegate.getEntityType()));
    }

    @Test
    public void testEventPublishingDelegation() throws Exception {
        final DummyEvent event = new DummyEvent();
        ((EventPublishingDataStore) dataStore).publishEvent(event);
        assertThat(listenerContext.getEvents(), hasSize(1));
        assertThat(listenerContext.getEvents().get(0).getEvent(), Matchers.<DataStoreEvent>is(event));
    }

    @Test(expectedExceptions = CorruptDataException.class)
    public void testSavingNullKey() throws Exception {
        dataStore.save(null, new Person());
    }

    @Test(expectedExceptions = CorruptDataException.class)
    public void testSavingNullEntity() throws Exception {
        dataStore.save("", null);
    }

    @Test
    public void testInsert() throws Exception {
        final String key = "k";
        final Person entity = new Person();
        dataStore.save(key, entity);
        assertThat(listenerContext.getEvents(), hasSize(2));
        assertThat(listenerContext.getEvents().get(0).getEvent(), is(instanceOf(BeforeInsertDataStoreEvent.class)));
        assertThat(listenerContext.getEvents().get(1).getEvent(), is(instanceOf(AfterInsertDataStoreEvent.class)));
        assertThat(delegateSpy.getRequests(), hasSize(1));
        final OperationRequest request = delegateSpy.getRequests().get(0);
        assertThat(request.getOperation(), is(Operation.SAVE));
        assertThat(request.getKey(), is(key));
        assertThat(request.getEntity(), is(entity));
        final EventTrigger before = listenerContext.getEvents().get(0);
        final EventTrigger after = listenerContext.getEvents().get(1);
        assertThat(before.getTimestamp(), is(lessThan(request.getTimestamp())));
        assertThat(request.getTimestamp(), is(lessThan(after.getTimestamp())));
        assertThat(before.getEvent().getDataStore(), Matchers.<DataStore<?, ?>>is(dataStore));
        assertThat(after.getEvent().getDataStore(), Matchers.<DataStore<?, ?>>is(dataStore));
    }

    @Test
    public void testUpdate() throws Exception {
        final String key = "k";
        final Person entity = new Person();
        dataStore.save(key, entity);
        listenerContext.getEvents().clear();
        delegateSpy.getRequests().clear();
        dataStore.save(key, entity);
        assertThat(listenerContext.getEvents(), hasSize(2));
        assertThat(listenerContext.getEvents().get(0).getEvent(), is(instanceOf(BeforeUpdateDataStoreEvent.class)));
        assertThat(listenerContext.getEvents().get(1).getEvent(), is(instanceOf(AfterUpdateDataStoreEvent.class)));
        assertThat(delegateSpy.getRequests(), hasSize(1));
        final OperationRequest request = delegateSpy.getRequests().get(0);
        assertThat(request.getOperation(), is(Operation.SAVE));
        assertThat(request.getKey(), is(key));
        assertThat(request.getEntity(), is(entity));
        final EventTrigger before = listenerContext.getEvents().get(0);
        final EventTrigger after = listenerContext.getEvents().get(1);
        assertThat(before.getTimestamp(), is(lessThan(request.getTimestamp())));
        assertThat(request.getTimestamp(), is(lessThan(after.getTimestamp())));
        assertThat(before.getEvent().getDataStore(), Matchers.<DataStore<?, ?>>is(dataStore));
        assertThat(after.getEvent().getDataStore(), Matchers.<DataStore<?, ?>>is(dataStore));
    }

    @Test
    public void testDeletingNonExistentKey() throws Exception {
        dataStore.delete("key");
        assertThat(delegateSpy.getRequests(), is(Matchers.<OperationRequest>empty()));
        assertThat(listenerContext.getEvents(), is(Matchers.<EventTrigger>empty()));
    }

    @Test
    public void testDelete() throws Exception {
        final String key = "k";
        final Person entity = new Person();
        dataStore.save(key, entity);
        listenerContext.getEvents().clear();
        delegateSpy.getRequests().clear();
        dataStore.delete(key);
        assertThat(listenerContext.getEvents(), hasSize(2));
        assertThat(listenerContext.getEvents().get(0).getEvent(), is(instanceOf(BeforeDeleteDataStoreEvent.class)));
        assertThat(listenerContext.getEvents().get(1).getEvent(), is(instanceOf(AfterDeleteDataStoreEvent.class)));
        assertThat(delegateSpy.getRequests(), hasSize(1));
        final OperationRequest request = delegateSpy.getRequests().get(0);
        assertThat(request.getOperation(), is(Operation.DELETE));
        assertThat(request.getKey(), is(key));
        assertThat(request.getEntity(), is(nullValue()));
        final EventTrigger before = listenerContext.getEvents().get(0);
        final EventTrigger after = listenerContext.getEvents().get(1);
        assertThat(before.getTimestamp(), is(lessThan(request.getTimestamp())));
        assertThat(request.getTimestamp(), is(lessThan(after.getTimestamp())));
        assertThat(before.getEvent().getDataStore(), Matchers.<DataStore<?, ?>>is(dataStore));
        assertThat(after.getEvent().getDataStore(), Matchers.<DataStore<?, ?>>is(dataStore));
    }

}