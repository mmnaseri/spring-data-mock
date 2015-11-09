package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class CountDataFunction implements DataFunction<Long> {

    @Override
    public <K extends Serializable, E> Long apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration repositoryConfiguration, List<E> selection) {
        return ((Integer) selection.size()).longValue();
    }

}
