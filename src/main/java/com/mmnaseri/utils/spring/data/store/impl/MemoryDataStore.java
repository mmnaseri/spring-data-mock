package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.store.DataStore;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class MemoryDataStore<K extends Serializable, E> implements DataStore<K, E> {
    
    private final ConcurrentMap<K, E> store = new ConcurrentHashMap<K, E>();
    private final Class<E> entityType;

    public MemoryDataStore(Class<E> entityType) {
        this.entityType = entityType;
    }

    @Override
    public boolean hasKey(K key) {
        return store.containsKey(key);
    }

    @Override
    public boolean save(K key, E entity) {
        return store.put(key, entity) == null;
    }

    @Override
    public boolean delete(K key) {
        if (store.containsKey(key)) {
            store.remove(key);
            return true;
        }
        return false;
    }

    @Override
    public E retrieve(K key) {
        if (store.containsKey(key)) {
            return store.get(key);
        }
        return null;
    }

    @Override
    public synchronized Collection<E> retrieveAll() {
        return new LinkedList<E>(store.values());
    }

    @Override
    public Class<E> getEntityType() {
        return entityType;
    }

}
