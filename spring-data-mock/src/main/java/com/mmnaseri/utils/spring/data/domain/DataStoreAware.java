package com.mmnaseri.utils.spring.data.domain;

import com.mmnaseri.utils.spring.data.store.DataStore;

/**
 * This interface is used to inject {@link DataStore the data store} into a concrete class aiming to provide method mapping
 * for a repository.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface DataStoreAware<E, K> {

    <J extends K, F extends E> void setDataStore(DataStore<J, F> dataStore);

}
