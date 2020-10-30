package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.error.CorruptDataException;
import com.mmnaseri.utils.spring.data.sample.mocks.EventTrigger;
import com.mmnaseri.utils.spring.data.sample.mocks.Operation;
import com.mmnaseri.utils.spring.data.sample.mocks.OperationRequest;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingDataStore;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingListenerContext;
import com.mmnaseri.utils.spring.data.sample.models.DummyEvent;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
@SuppressWarnings("WeakerAccess")
public class EventPublishingDataStoreTest {

  private static final AtomicLong counter = new AtomicLong(0);
  private RepositoryMetadata repositoryMetadata;
  private DataStore<String, Person> delegate;
  private SpyingDataStore<String, Person> delegateSpy;
  private DataStore<String, Person> dataStore;
  private SpyingListenerContext listenerContext;

  @BeforeMethod
  public void setUp() {
    repositoryMetadata =
        new ImmutableRepositoryMetadata(
            String.class, Person.class, SimplePersonRepository.class, "id");
    listenerContext = new SpyingListenerContext(counter);
    delegate = new MemoryDataStore<>(Person.class);
    delegateSpy = new SpyingDataStore<>(delegate, counter);
    dataStore = new EventPublishingDataStore<>(delegateSpy, repositoryMetadata, listenerContext);
  }

  @Test
  public void testHasKeyDelegation() {
    final String key = "1";
    delegate.save(key, new Person());
    assertThat(dataStore.hasKey(key), is(true));
  }

  @Test
  public void testRetrieveDelegation() {
    final String key = "key";
    assertThat(dataStore.retrieve(key), is(delegate.retrieve(key)));
    delegate.save(key, new Person());
    assertThat(dataStore.retrieve(key), is(delegate.retrieve(key)));
  }

  @Test
  public void testRetrieveAllDelegation() {
    final String k1 = "k1";
    final String k2 = "k2";
    assertThat(dataStore.retrieveAll(), is(delegate.retrieveAll()));
    delegate.save(k1, new Person());
    delegate.save(k2, new Person());
    assertThat(dataStore.retrieveAll(), is(delegate.retrieveAll()));
  }

  @Test
  public void testEntityTypeDelegation() {
    assertThat(dataStore.getEntityType(), Matchers.<Class<Person>>is(delegate.getEntityType()));
  }

  @Test
  public void testEventPublishingDelegation() {
    final DummyEvent event = new DummyEvent();
    ((EventPublishingDataStore) dataStore).publishEvent(event);
    assertThat(listenerContext.getEvents(), hasSize(1));
    assertThat(listenerContext.getEvents().get(0).getEvent(), Matchers.is(event));
  }

  @Test
  public void testKeysDelegation() {
    final AtomicBoolean called = new AtomicBoolean(false);
    final DataStore<String, Person> localDataStore =
        new EventPublishingDataStore<>(
            new MemoryDataStore<String, Person>(Person.class) {

              @Override
              public Collection<String> keys() {
                called.set(true);
                return super.keys();
              }
            },
            repositoryMetadata,
            listenerContext);
    assertThat(called.get(), is(false));
    localDataStore.keys();
    assertThat(called.get(), is(true));
  }

  @Test(expectedExceptions = CorruptDataException.class)
  public void testSavingNullKey() {
    dataStore.save(null, new Person());
  }

  @Test(expectedExceptions = CorruptDataException.class)
  public void testSavingNullEntity() {
    dataStore.save("", null);
  }

  @Test
  public void testInsert() {
    final String key = "k";
    final Person entity = new Person();
    dataStore.save(key, entity);
    assertThat(listenerContext.getEvents(), hasSize(2));
    assertThat(
        listenerContext.getEvents().get(0).getEvent(),
        is(instanceOf(BeforeInsertDataStoreEvent.class)));
    assertThat(
        listenerContext.getEvents().get(1).getEvent(),
        is(instanceOf(AfterInsertDataStoreEvent.class)));
    assertThat(delegateSpy.getRequests(), hasSize(2));
    final OperationRequest save = delegateSpy.getRequests().get(1);
    assertThat(save.getOperation(), is(Operation.SAVE));
    assertThat(save.getKey(), Matchers.is(key));
    assertThat(save.getEntity(), Matchers.is(entity));
    final EventTrigger before = listenerContext.getEvents().get(0);
    final EventTrigger after = listenerContext.getEvents().get(1);
    assertThat(before.getTimestamp(), is(lessThan(save.getTimestamp())));
    assertThat(save.getTimestamp(), is(lessThan(after.getTimestamp())));
    assertThat(before.getEvent().getDataStore(), Matchers.is(dataStore));
    assertThat(after.getEvent().getDataStore(), Matchers.is(dataStore));
  }

  @Test
  public void testUpdate() {
    final String key = "k";
    final Person entity = new Person();
    dataStore.save(key, entity);
    listenerContext.reset();
    delegateSpy.reset();
    dataStore.save(key, entity);
    assertThat(listenerContext.getEvents(), hasSize(2));
    assertThat(
        listenerContext.getEvents().get(0).getEvent(),
        is(instanceOf(BeforeUpdateDataStoreEvent.class)));
    assertThat(
        listenerContext.getEvents().get(1).getEvent(),
        is(instanceOf(AfterUpdateDataStoreEvent.class)));
    assertThat(delegateSpy.getRequests(), hasSize(2));
    final OperationRequest request = delegateSpy.getRequests().get(1);
    assertThat(request.getOperation(), is(Operation.SAVE));
    assertThat(request.getKey(), Matchers.is(key));
    assertThat(request.getEntity(), Matchers.is(entity));
    final EventTrigger before = listenerContext.getEvents().get(0);
    final EventTrigger after = listenerContext.getEvents().get(1);
    assertThat(before.getTimestamp(), is(lessThan(request.getTimestamp())));
    assertThat(request.getTimestamp(), is(lessThan(after.getTimestamp())));
    assertThat(before.getEvent().getDataStore(), Matchers.is(dataStore));
    assertThat(after.getEvent().getDataStore(), Matchers.is(dataStore));
  }

  @Test
  public void testDeletingNonExistentKey() {
    dataStore.delete("key");
    assertThat(delegateSpy.getRequests(), hasSize(1));
    assertThat(listenerContext.getEvents(), is(Matchers.empty()));
  }

  @Test
  public void testDelete() {
    final String key = "k";
    final Person entity = new Person();
    dataStore.save(key, entity);
    listenerContext.reset();
    delegateSpy.reset();
    dataStore.delete(key);
    assertThat(listenerContext.getEvents(), hasSize(2));
    assertThat(
        listenerContext.getEvents().get(0).getEvent(),
        is(instanceOf(BeforeDeleteDataStoreEvent.class)));
    assertThat(
        listenerContext.getEvents().get(1).getEvent(),
        is(instanceOf(AfterDeleteDataStoreEvent.class)));
    assertThat(delegateSpy.getRequests(), hasSize(3));
    final OperationRequest request = delegateSpy.getRequests().get(2);
    assertThat(request.getOperation(), is(Operation.DELETE));
    assertThat(request.getKey(), Matchers.is(key));
    assertThat(request.getEntity(), is(nullValue()));
    final EventTrigger before = listenerContext.getEvents().get(0);
    final EventTrigger after = listenerContext.getEvents().get(1);
    assertThat(before.getTimestamp(), is(lessThan(request.getTimestamp())));
    assertThat(request.getTimestamp(), is(lessThan(after.getTimestamp())));
    assertThat(before.getEvent().getDataStore(), Matchers.is(dataStore));
    assertThat(after.getEvent().getDataStore(), Matchers.is(dataStore));
  }

  @Test
  public void testTruncating() {
    final String k1 = "k1";
    final String k2 = "k2";
    assertThat(dataStore.retrieveAll(), is(delegate.retrieveAll()));
    delegate.save(k1, new Person());
    delegate.save(k2, new Person());
    assertThat(dataStore.retrieveAll(), is(delegate.retrieveAll()));
    dataStore.truncate();
    assertThat(delegate.retrieveAll(), is(Matchers.empty()));
  }
}
