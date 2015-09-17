package com.mmnaseri.utils.spring;

import java.util.Collection;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface DataStore<K, E> {

    void save(K key, E entity);

    void delete(K key);

    E retrieve(K key);

    Collection<E> retrieveAll();

}
