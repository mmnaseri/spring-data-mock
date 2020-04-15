package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;

import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class DataFunctionInvocation<K, E> {

    private final DataStore<K, E> dataStore;
    private final QueryDescriptor query;
    private final RepositoryConfiguration repositoryConfiguration;
    private final List<E> selection;

    public DataFunctionInvocation(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration repositoryConfiguration, List<E> selection) {
        this.dataStore = dataStore;
        this.query = query;
        this.repositoryConfiguration = repositoryConfiguration;
        this.selection = selection;
    }

    public DataStore<K, E> getDataStore() {
        return dataStore;
    }

    public QueryDescriptor getQuery() {
        return query;
    }

    public RepositoryConfiguration getRepositoryConfiguration() {
        return repositoryConfiguration;
    }

    public List<E> getSelection() {
        return selection;
    }

}
