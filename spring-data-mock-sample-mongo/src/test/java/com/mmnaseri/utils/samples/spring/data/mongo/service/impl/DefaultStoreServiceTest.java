package com.mmnaseri.utils.samples.spring.data.mongo.service.impl;

import com.mmnaseri.utils.samples.spring.data.mongo.model.Store;
import com.mmnaseri.utils.samples.spring.data.mongo.repository.StoreRepository;
import com.mmnaseri.utils.spring.data.dsl.mock.RepositoryMockBuilder;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Collection;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

public class DefaultStoreServiceTest {

    private StoreRepository repository;
    private DefaultStoreService service;

    @BeforeMethod
    public void setUp() {
        repository = new RepositoryMockBuilder().mock(StoreRepository.class);
        service = new DefaultStoreService(repository);
    }

    @Test
    public void testCreateOne() {
        final Store store = service.create("My Store");
        assertThat(store, notNullValue());
        assertThat(store.getName(), is("My Store"));
    }

    @Test
    public void testCreateMultiple() {
        final Collection<Store> stores = service.create("Store #1", "Store #2");
    }
}