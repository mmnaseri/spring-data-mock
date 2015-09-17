package com.mmnaseri.utils.spring.impl;

import com.mmnaseri.utils.spring.DataStore;

import java.util.Collection;
import java.util.LinkedList;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class MemoryDataStore<K, E> implements DataStore<K, E> {
    
    private final ConcurrentMap<K, E> store = new ConcurrentHashMap<K, E>();
    
    @Override
    public void save(K key, E entity) {
        store.put(key, entity);
    }

    @Override
    public void delete(K key) {
        if (store.containsKey(key)) {
            store.remove(key);
        }
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
    
}
