package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;

import java.io.Serializable;
import java.util.List;

/**
 * This data function provides support for the {@literal count} aggregator over a selection.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("WeakerAccess")
public class CountDataFunction implements DataFunction<Long> {

    @Override
    public <K extends Serializable, E> Long apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration repositoryConfiguration, List<E> selection) {
        if (selection == null) {
            throw new InvalidArgumentException("Selection cannot be null");
        }
        return ((Integer) selection.size()).longValue();
    }

}
