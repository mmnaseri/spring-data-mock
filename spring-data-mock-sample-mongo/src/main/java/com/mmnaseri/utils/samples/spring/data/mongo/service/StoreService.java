package com.mmnaseri.utils.samples.spring.data.mongo.service;

import com.mmnaseri.utils.samples.spring.data.mongo.model.Store;

import java.util.Collection;

public interface StoreService {

    Store create(String name);

    Collection<Store> create(String... names);

}
