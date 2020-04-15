package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.List;

/**
 * This data function provides support for the {@literal count} aggregator over a selection.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("WeakerAccess")
public class CountDataFunction implements DataFunction<Long> {

    private static final Log log = LogFactory.getLog(CountDataFunction.class);

    @Override
    public <K, E> Long apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration repositoryConfiguration, List<E> selection) {
        if (selection == null) {
            log.error("Cannot calculate the count if the selection is null");
            throw new InvalidArgumentException("Selection cannot be null");
        }
        return ((Integer) selection.size()).longValue();
    }

}
