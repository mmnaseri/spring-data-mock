package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.store.DataStore;

import java.util.List;

/**
 * <p>This interface encapsulates a data function. A data function is a function that applies to an already aggregated
 * selection of entities, and can return any value.</p>
 *
 * <p>An example for a data function would be returning the number of items from a selection (count function) or
 * duplicating all the selected entities.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface DataFunction<R> {

    <K, E> R apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration configuration,
                   List<E> selection);

}
