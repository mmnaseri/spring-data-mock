package com.mmnaseri.utils.spring.data.store;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;

/**
 * This interface encapsulates a single operation taking place on a data store
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public interface DataStoreOperation<R, K, E> {

    /**
     * Called to trigger the actual operation
     * @param store            the data store on which this operation is taking place
     * @param configuration    the configuration for the data store
     * @param invocation       the invocation that triggered this operation
     * @return the result of the operation
     */
    R execute(DataStore<K, E> store, RepositoryConfiguration configuration, Invocation invocation);

}
