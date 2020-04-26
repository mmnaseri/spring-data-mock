package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.sample.models.Address;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.models.State;
import com.mmnaseri.utils.spring.data.sample.models.Zip;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.hamcrest.Matchers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/11/16, 1:58 PM)
 */
public class DefaultPagingAndSortingRepositoryTest {

    private DefaultPagingAndSortingRepository repository;
    private DataStore<String, Person> dataStore;

    @BeforeMethod
    public void setUp() throws Exception {
        dataStore = new MemoryDataStore<>(Person.class);
        repository = new DefaultPagingAndSortingRepository();
        repository.setDataStore(dataStore);
        dataStore.save("1", new Person().setId("1").setAddress(new Address().setZip(new Zip().setArea("A1"))
                                                                            .setCity("Seattle").setState(
                        new State().setName("Washington").setAbbreviation("WA"))));
        dataStore.save("2", new Person().setId("2").setAddress(new Address().setZip(new Zip().setArea("A2"))
                                                                            .setCity("Edmonds").setState(
                        new State().setName("Washington").setAbbreviation("WA"))));
        dataStore.save("3", new Person().setId("3").setAddress(new Address().setZip(new Zip().setArea("A3"))
                                                                            .setCity("Seattle").setState(
                        new State().setName("Washington").setAbbreviation("WA"))));
        dataStore.save("4", new Person().setId("4").setAddress(new Address().setZip(new Zip().setArea("A1"))
                                                                            .setCity("Kirkland").setState(
                        new State().setName("Washington").setAbbreviation("WA"))));
        dataStore.save("5", new Person().setId("5").setAddress(new Address().setZip(new Zip().setArea("A2"))
                                                                            .setCity("Portland").setState(
                        new State().setName("Oregon").setAbbreviation("OR"))));
        dataStore.save("6", new Person().setId("6").setAddress(new Address().setZip(new Zip().setArea("A3"))
                                                                            .setCity("Portland").setState(
                        new State().setName("Oregon").setAbbreviation("OR"))));
        dataStore.save("7", new Person().setId("7").setAddress(new Address().setZip(new Zip().setArea("A1"))
                                                                            .setCity("Spokane").setState(
                        new State().setName("Washington").setAbbreviation("WA"))));
        dataStore.save("8", new Person().setId("8").setAddress(new Address().setZip(new Zip().setArea("A2"))
                                                                            .setCity("Seattle").setState(
                        new State().setName("Washington").setAbbreviation("WA"))));
    }

    @Test
    public void testFindAllWithNullSort() throws Exception {
        final List<?> found = repository.findAll(((Sort) null));
        assertThat(found, is(notNullValue()));
        assertThat(found, hasSize(dataStore.retrieveAll().size()));
    }

    @Test
    public void testFindAllWithSort() throws Exception {
        final List<?> found = repository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "address.city"),
                                                         new Sort.Order(Sort.Direction.DESC, "address.zip.area")));
        assertThat(found, hasSize(dataStore.retrieveAll().size()));
        assertThat(found.get(0), Matchers.<Object>is(dataStore.retrieve("2")));
        assertThat(found.get(1), Matchers.<Object>is(dataStore.retrieve("4")));
        assertThat(found.get(2), Matchers.<Object>is(dataStore.retrieve("6")));
        assertThat(found.get(3), Matchers.<Object>is(dataStore.retrieve("5")));
        assertThat(found.get(4), Matchers.<Object>is(dataStore.retrieve("3")));
        assertThat(found.get(5), Matchers.<Object>is(dataStore.retrieve("8")));
        assertThat(found.get(6), Matchers.<Object>is(dataStore.retrieve("1")));
        assertThat(found.get(7), Matchers.<Object>is(dataStore.retrieve("7")));
    }

    @Test
    public void testFindAllWithPagingAndNoSorting() throws Exception {
        final Page page = repository.findAll(PageRequest.of(2, 3));
        assertThat(page.getTotalElements(), is(8L));
        assertThat(page.getTotalPages(), is(3));
        assertThat(page.getNumber(), is(2));
        assertThat(page.getSize(), is(3));
        assertThat(page.getNumberOfElements(), is(2));
    }

    @Test
    public void testFindAllWithPagingAndSorting() throws Exception {
        final Page page = repository.findAll(PageRequest.of(2, 3,
                                                            Sort.by(new Sort.Order(Sort.Direction.ASC, "address.city"),
                                                                    new Sort.Order(Sort.Direction.DESC,
                                                                                   "address.zip.area"))));
        assertThat(page.getTotalElements(), is(8L));
        assertThat(page.getTotalPages(), is(3));
        assertThat(page.getNumber(), is(2));
        assertThat(page.getSize(), is(3));
        assertThat(page.getNumberOfElements(), is(2));
        final List<?> found = page.getContent();
        assertThat(found.get(0), Matchers.<Object>is(dataStore.retrieve("1")));
        assertThat(found.get(1), Matchers.<Object>is(dataStore.retrieve("7")));
    }

    @Test
    public void testWithNullsFirst() throws Exception {
        dataStore.save("9", new Person().setId("9").setAddress(new Address().setZip(new Zip().setArea(null))
                                                                            .setCity("Spokane").setState(
                        new State().setName("Washington").setAbbreviation("WA"))));
        final List<?> found = repository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "address.city"),
                                                         new Sort.Order(Sort.Direction.DESC, "address.zip.area",
                                                                        Sort.NullHandling.NULLS_FIRST)));
        assertThat(found, hasSize(dataStore.retrieveAll().size()));
        assertThat(found.get(0), Matchers.<Object>is(dataStore.retrieve("2")));
        assertThat(found.get(1), Matchers.<Object>is(dataStore.retrieve("4")));
        assertThat(found.get(2), Matchers.<Object>is(dataStore.retrieve("6")));
        assertThat(found.get(3), Matchers.<Object>is(dataStore.retrieve("5")));
        assertThat(found.get(4), Matchers.<Object>is(dataStore.retrieve("3")));
        assertThat(found.get(5), Matchers.<Object>is(dataStore.retrieve("8")));
        assertThat(found.get(6), Matchers.<Object>is(dataStore.retrieve("1")));
        assertThat(found.get(7), Matchers.<Object>is(dataStore.retrieve("7")));
        assertThat(found.get(8), Matchers.<Object>is(dataStore.retrieve("9")));
    }

    @Test
    public void testWithNullsLast() throws Exception {
        dataStore.save("9", new Person().setId("9").setAddress(new Address().setZip(new Zip().setArea(null))
                                                                            .setCity("Spokane").setState(
                        new State().setName("Washington").setAbbreviation("WA"))));
        final List<?> found = repository.findAll(Sort.by(new Sort.Order(Sort.Direction.ASC, "address.city"),
                                                         new Sort.Order(Sort.Direction.DESC, "address.zip.area",
                                                                        Sort.NullHandling.NULLS_LAST)));
        assertThat(found, hasSize(dataStore.retrieveAll().size()));
        assertThat(found.get(0), Matchers.<Object>is(dataStore.retrieve("2")));
        assertThat(found.get(1), Matchers.<Object>is(dataStore.retrieve("4")));
        assertThat(found.get(2), Matchers.<Object>is(dataStore.retrieve("6")));
        assertThat(found.get(3), Matchers.<Object>is(dataStore.retrieve("5")));
        assertThat(found.get(4), Matchers.<Object>is(dataStore.retrieve("3")));
        assertThat(found.get(5), Matchers.<Object>is(dataStore.retrieve("8")));
        assertThat(found.get(6), Matchers.<Object>is(dataStore.retrieve("1")));
        assertThat(found.get(7), Matchers.<Object>is(dataStore.retrieve("9")));
        assertThat(found.get(8), Matchers.<Object>is(dataStore.retrieve("7")));
    }

}