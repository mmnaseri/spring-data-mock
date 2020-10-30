package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.error.DataStoreNotFoundException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreRegistry;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This is the default implementation of the data store registry that supports caching a data store
 * based on the type of entity the data store supports.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class DefaultDataStoreRegistry implements DataStoreRegistry {

  private static final Log log = LogFactory.getLog(DefaultDataStoreRegistry.class);
  private final Map<Class<?>, DataStore<?, ?>> dataStores = new ConcurrentHashMap<>();

  @Override
  public <E, K> void register(DataStore<K, E> dataStore) {
    log.info("Registering a data store for type " + dataStore.getEntityType());
    dataStores.put(dataStore.getEntityType(), dataStore);
  }

  @Override
  public <E, K> DataStore<K, E> getDataStore(Class<E> entityType) {
    if (!dataStores.containsKey(entityType)) {
      log.error("There is no data store registered for entity type " + entityType);
      log.debug("Registered data types are " + dataStores.keySet());
      throw new DataStoreNotFoundException(entityType);
    }
    //noinspection unchecked
    return (DataStore<K, E>) dataStores.get(entityType);
  }

  @Override
  public boolean has(Class<?> entityType) {
    return dataStores.containsKey(entityType);
  }
}
