package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.error.DataStoreException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * This is the default, most basic implementation provided for a data store that stores entities in an in-memory
 * {@link java.util.Map map} by mapping entity keys to entities.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public class MemoryDataStore<K extends Serializable, E> implements DataStore<K, E> {

    private static final Log log = LogFactory.getLog(MemoryDataStore.class);
    private final ConcurrentMap<K, E> store = new ConcurrentHashMap<>();
    private final Class<E> entityType;

    public MemoryDataStore(Class<E> entityType) {
        this.entityType = entityType;
    }

    @Override
    public boolean hasKey(K key) {
        log.info("Looking for an object with key " + key);
        return store.containsKey(key);
    }

    @Override
    public boolean save(K key, E entity) {
        if (key == null) {
            log.error("Asked to save an entity with a null key");
            throw new DataStoreException(entityType, "Cannot save an entity with a null key");
        } else if (entity == null) {
            log.error("Asked to save a null value into the data store");
            throw new DataStoreException(entityType, "Cannot save a null entity");
        }
        log.info("Attempting to save entity with key " + key);
        boolean saved = store.put(key, entity) == null;
        log.debug("Entity was " + (!saved ? "not " : "") + "saved under key " + key);
        return saved;
    }

    @Override
    public boolean delete(K key) {
        if (key == null) {
            log.error("Asked to delete an entity with a null key for reference");
            throw new DataStoreException(entityType, "Cannot delete an entity with a null key");
        }
        if (store.containsKey(key)) {
            log.info("Deleting entity under key " + key);
            store.remove(key);
            return true;
        } else {
            log.info("No entity was found to delete under key " + key);
        }
        return false;
    }

    @Override
    public E retrieve(K key) {
        if (key == null) {
            log.error("Asked to retrieve an entity from a null key");
            throw new DataStoreException(entityType, "Cannot retrieve an entity with a null key");
        }
        if (store.containsKey(key)) {
            log.info("Retrieving entity from key " + key);
            return store.get(key);
        } else {
            log.info("No entity was found to return under key " + key);
        }
        return null;
    }

    @Override
    public Collection<K> keys() {
        return new LinkedList<>(store.keySet());
    }

    @Override
    public synchronized Collection<E> retrieveAll() {
        log.info("Retrieving all entities from the data store");
        return new LinkedList<>(store.values());
    }

    @Override
    public Class<E> getEntityType() {
        return entityType;
    }

}
