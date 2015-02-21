package com.agileapes.utils.spring;

import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:13)
 */
public interface RepositoryDescriptor<E, K extends Serializable, R extends Repository<E, K>> {

    Class<R> getRepositoryType();

    Map<K, E> getData();

    Map<MethodDescriptor, DataOperation<E, K>> getOperations();

    String getKeyProperty();

    KeyGeneration getKeyGeneration();

    Class<E> getEntityType();

    Class<K> getKeyType();

}
