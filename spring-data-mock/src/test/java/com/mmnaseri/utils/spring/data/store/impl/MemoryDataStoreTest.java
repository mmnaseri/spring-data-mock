package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.error.DataStoreException;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/9/16)
 */
public class MemoryDataStoreTest {

    private MemoryDataStore<String, Person> dataStore;

    @BeforeMethod
    public void setUp() throws Exception {
        dataStore = new MemoryDataStore<>(Person.class);
    }

    @Test
    public void testEntityType() throws Exception {
        assertThat(dataStore.getEntityType(), is(equalTo(Person.class)));
    }

    @Test
    public void testSaveAndRetrieve() throws Exception {
        assertThat(dataStore.retrieveAll(), is(Matchers.<Person>empty()));
        final Person person = new Person();
        dataStore.save("key", person);
        assertThat(dataStore.retrieveAll(), hasSize(1));
    }

    @Test
    public void testRetrievalByKey() throws Exception {
        final Person person = new Person();
        final String key = "key";
        assertThat(dataStore.retrieve(key), is(nullValue()));
        assertThat(dataStore.retrieve(key + "x"), is(nullValue()));
        dataStore.save(key, person);
        assertThat(dataStore.retrieve(key), is(person));
        assertThat(dataStore.retrieve(key + "x"), is(nullValue()));
    }

    @Test
    public void testExistence() throws Exception {
        final Person person = new Person();
        final String key = "key";
        assertThat(dataStore.hasKey(key), is(false));
        assertThat(dataStore.hasKey(key + "x"), is(false));
        dataStore.save(key, person);
        assertThat(dataStore.hasKey(key), is(true));
        assertThat(dataStore.hasKey(key + "x"), is(false));
    }

    @Test
    public void testSaveAndDelete() throws Exception {
        final Person person1 = new Person();
        final Person person2 = new Person();
        final String key1 = "key1";
        final String key2 = "key2";
        assertThat(dataStore.retrieveAll(), is(Matchers.<Person>empty()));
        dataStore.save(key1, person1);
        assertThat(dataStore.hasKey(key1), is(true));
        dataStore.save(key2, person2);
        assertThat(dataStore.hasKey(key2), is(true));
        assertThat(dataStore.retrieveAll(), hasSize(2));
        assertThat(dataStore.retrieveAll(), containsInAnyOrder(person1, person2));
        dataStore.delete(key1 + "x");
        assertThat(dataStore.hasKey(key2), is(true));
        assertThat(dataStore.retrieveAll(), hasSize(2));
        assertThat(dataStore.retrieveAll(), containsInAnyOrder(person1, person2));
        dataStore.delete(key1);
        assertThat(dataStore.hasKey(key1), is(false));
        assertThat(dataStore.hasKey(key2), is(true));
        assertThat(dataStore.retrieveAll(), hasSize(1));
        assertThat(dataStore.retrieveAll(), containsInAnyOrder(person2));
        dataStore.delete(key2);
        assertThat(dataStore.hasKey(key1), is(false));
        assertThat(dataStore.hasKey(key2), is(false));
        assertThat(dataStore.retrieveAll(), is(Matchers.<Person>empty()));
    }

    @Test
    public void testKeys() throws Exception {
        dataStore.save("1", new Person());
        dataStore.save("2", new Person());
        dataStore.save("3", new Person());
        assertThat(dataStore.keys(), containsInAnyOrder("1", "2", "3"));
    }

    @Test(expectedExceptions = DataStoreException.class)
    public void testSavingWithNullKey() throws Exception {
        dataStore.save(null, new Person());
    }

    @Test(expectedExceptions = DataStoreException.class)
    public void testSavingWithNullEntity() throws Exception {
        dataStore.save("1", null);
    }

    @Test(expectedExceptions = DataStoreException.class)
    public void testDeletingWithNullKey() throws Exception {
        dataStore.delete(null);
    }

    @Test(expectedExceptions = DataStoreException.class)
    public void testRetrievingWithNullKey() throws Exception {
        dataStore.retrieve(null);
    }

    @Test
    public void testTruncating() throws Exception {
        dataStore.save("1", new Person());
        dataStore.save("2", new Person());
        dataStore.save("3", new Person());
        assertThat(dataStore.retrieveAll(), hasSize(3));
        dataStore.truncate();
        assertThat(dataStore.retrieveAll(), is(Matchers.<Person>empty()));
    }

}