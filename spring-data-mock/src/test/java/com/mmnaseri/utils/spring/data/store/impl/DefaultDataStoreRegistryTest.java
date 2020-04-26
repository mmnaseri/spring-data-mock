package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.error.DataStoreNotFoundException;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class DefaultDataStoreRegistryTest {

    @Test
    public void testRegisteringDataStore() throws Exception {
        final DefaultDataStoreRegistry registry = new DefaultDataStoreRegistry();
        final MemoryDataStore<Object, Person> dataStore = new MemoryDataStore<>(Person.class);
        registry.register(dataStore);
        assertThat(registry.getDataStore(Person.class), Matchers.<DataStore<Object, Person>>is(dataStore));
    }

    @Test
    public void testOverridingDataStore() throws Exception {
        final DefaultDataStoreRegistry registry = new DefaultDataStoreRegistry();
        final MemoryDataStore<Object, Person> first = new MemoryDataStore<>(Person.class);
        final MemoryDataStore<Object, Person> second = new MemoryDataStore<>(Person.class);
        registry.register(first);
        registry.register(second);
        assertThat(registry.getDataStore(Person.class), Matchers.<DataStore<Object, Person>>is(second));
    }

    @Test(expectedExceptions = DataStoreNotFoundException.class)
    public void testLookingForInvalidDataStore() throws Exception {
        final DefaultDataStoreRegistry registry = new DefaultDataStoreRegistry();
        registry.getDataStore(Person.class);
    }

}