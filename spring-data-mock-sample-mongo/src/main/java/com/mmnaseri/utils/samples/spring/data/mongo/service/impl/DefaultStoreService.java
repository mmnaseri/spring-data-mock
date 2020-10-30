package com.mmnaseri.utils.samples.spring.data.mongo.service.impl;

import com.mmnaseri.utils.samples.spring.data.mongo.model.Store;
import com.mmnaseri.utils.samples.spring.data.mongo.repository.StoreRepository;
import com.mmnaseri.utils.samples.spring.data.mongo.service.StoreService;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import static java.util.stream.Collectors.toList;

public class DefaultStoreService implements StoreService {

  private final StoreRepository repository;

  public DefaultStoreService(final StoreRepository repository) {
    this.repository = repository;
  }

  @Override
  public Store create(final String name) {
    final Store store = new Store().setName(name);
    return repository.save(store);
  }

  @Override
  public Collection<Store> create(final String... names) {
    final List<Store> stores =
        Arrays.stream(names).map(name -> new Store().setName(name)).collect(toList());
    return repository.insert(stores);
  }
}
