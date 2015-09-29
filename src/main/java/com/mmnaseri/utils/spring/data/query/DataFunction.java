package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.store.DataStore;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface DataFunction<R> {

    <K extends Serializable, E> R apply(DataStore<K, E> dataStore, QueryDescriptor query, List<E> selection);

}
