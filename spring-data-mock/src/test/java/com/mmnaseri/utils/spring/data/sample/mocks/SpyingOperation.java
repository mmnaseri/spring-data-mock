package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:37 PM)
 */
public class SpyingOperation implements DataStoreOperation<Object, String, Person> {

    private DataStore<String, Person> store;
    private RepositoryConfiguration configuration;
    private Invocation invocation;
    private final Object result;

    public SpyingOperation(Object result) {
        this.result = result;
    }

    @Override
    public Object execute(DataStore<String, Person> store, RepositoryConfiguration configuration,
                          Invocation invocation) {
        this.store = store;
        this.configuration = configuration;
        this.invocation = invocation;
        return result;
    }

    public DataStore<String, Person> getStore() {
        return store;
    }

    public RepositoryConfiguration getConfiguration() {
        return configuration;
    }

    public Invocation getInvocation() {
        return invocation;
    }

}
