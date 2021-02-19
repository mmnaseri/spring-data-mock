package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.DefaultMethodPersonRepository;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder.builder;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class DefaultMethodTest {

  private DefaultMethodPersonRepository mock;
  private DataStore<Object, Person> dataStore;

  @BeforeMethod
  public void setUp() {
    dataStore = new MemoryDataStore<>(Person.class);
    mock = builder().registerDataStore(dataStore).mock(DefaultMethodPersonRepository.class);
  }

  @Test
  public void testCallingDefaultMethod() {
    dataStore.save("1", new Person().setId("1").setFirstName("Mohammad").setAge(100));
    dataStore.save("2", new Person().setId("2").setFirstName("Moses").setAge(110));
    dataStore.save("3", new Person().setId("3").setFirstName("Milad").setAge(30));

    List<Person> interestingPeople = mock.theInterestingPeople();

    assertThat(interestingPeople, is(notNullValue()));
    assertThat(interestingPeople, containsInAnyOrder(dataStore.retrieve("1"), dataStore.retrieve("2")));
  }

}
