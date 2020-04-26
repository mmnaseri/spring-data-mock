package com.mmnaseri.utils.samples.spring.data.jpa.service.impl;

import com.mmnaseri.utils.samples.spring.data.jpa.model.Plane;
import com.mmnaseri.utils.samples.spring.data.jpa.repository.PlaneRepository;
import com.mmnaseri.utils.samples.spring.data.jpa.utils.RepositoryConfigUtils;
import com.mmnaseri.utils.spring.data.dsl.mock.RepositoryMockBuilder;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 11:41 PM)
 */
public class DefaultPlaneServiceTest {

    private PlaneRepository repository;
    private DefaultPlaneService service;

    @BeforeMethod
    public void setUp() throws Exception {
        final RepositoryFactoryConfiguration configuration = RepositoryConfigUtils.getConfiguration();
        repository = new RepositoryMockBuilder()
                .useConfiguration(configuration)
                .mock(PlaneRepository.class);
        service = new DefaultPlaneService(repository);
    }

    @AfterMethod
    public void tearDown() throws Exception {
        final RepositoryFactoryConfiguration configuration = RepositoryConfigUtils.getConfiguration();
        //because the configuration is now shared, it means that the data store registry is shared across all
        //the tests, too.
        //This is the same as using a shared database for doing all the tests. So, at the end of the tests we need
        //to clear the database after us like using a regular data store
        final DataStore<Object, Plane> dataStore = configuration.getDataStoreRegistry().getDataStore(Plane.class);
        dataStore.truncate();
    }

    @Test
    public void testCreate() throws Exception {
        assertThat(repository.count(), is(0L));
        final String model = "F-22";
        final String serial = "123456";
        final Long id = service.create(model, serial);
        assertThat(id, is(notNullValue()));
        final Plane loaded = repository.findById(id).orElse(null);
        assertThat(loaded, is(notNullValue()));
        assertThat(loaded.getModel(), is(model));
        assertThat(loaded.getSerial(), is(serial));
    }

    @Test
    public void testLookupById() throws Exception {
        final Plane entity = new Plane();
        entity.setModel("Boeing 747");
        entity.setSerial("123456");
        entity.setCapacity(1000);
        final Plane saved = repository.save(entity);
        final String model = service.lookup(saved.getId());
        assertThat(model, is(notNullValue()));
        assertThat(model, is(entity.getModel()));
    }

    @Test
    public void testLookupBySerial() throws Exception {
        final Plane entity = new Plane();
        entity.setModel("Boeing 747");
        entity.setSerial("123456");
        entity.setCapacity(1000);
        repository.save(entity);
        final String model = service.lookup(entity.getSerial());
        assertThat(model, is(notNullValue()));
        assertThat(model, is(entity.getModel()));
    }

}