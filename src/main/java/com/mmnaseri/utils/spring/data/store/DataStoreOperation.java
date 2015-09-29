package com.mmnaseri.utils.spring.data.store;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface DataStoreOperation<R, K extends Serializable, E> {

    R execute(DataStore<K, E> store, RepositoryMetadata repositoryMetadata, Invocation invocation);

}
