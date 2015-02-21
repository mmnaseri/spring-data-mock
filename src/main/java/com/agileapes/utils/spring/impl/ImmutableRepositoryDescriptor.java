package com.agileapes.utils.spring.impl;

import com.agileapes.utils.spring.DataOperation;
import com.agileapes.utils.spring.KeyGeneration;
import com.agileapes.utils.spring.MethodDescriptor;
import com.agileapes.utils.spring.RepositoryDescriptor;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:33)
 */
public class ImmutableRepositoryDescriptor<E, K extends Serializable, R extends Repository<E, K>> implements RepositoryDescriptor<E, K, R> {

    private final Class<R> repositoryType;
    private final Map<K, E> data;
    private final Map<MethodDescriptor, DataOperation<E, K>> operations;
    private final String keyProperty;
    private final KeyGeneration keyGeneration;
    private final Class<E> entityType;
    private final Class<K> keyType;

    public ImmutableRepositoryDescriptor(RepositoryDescriptor<E, K, R> descriptor) {
        this(descriptor.getRepositoryType(), descriptor.getData(), descriptor.getOperations(), descriptor.getKeyProperty(), descriptor.getKeyGeneration());
    }

    public ImmutableRepositoryDescriptor(Class<R> repositoryType, Map<K, E> data, Map<MethodDescriptor, DataOperation<E, K>> operations, String keyProperty, KeyGeneration keyGeneration) {
        this.repositoryType = repositoryType;
        this.data = data == null ? new HashMap<K, E>() : new HashMap<K, E>(data);
        this.operations = operations == null ? new HashMap<MethodDescriptor, DataOperation<E, K>>() : new HashMap<MethodDescriptor, DataOperation<E, K>>(operations);
        this.keyProperty = keyProperty;
        this.keyGeneration = keyGeneration;
        final Class<?>[] typeArguments = GenericTypeResolver.resolveTypeArguments(repositoryType, Repository.class);
        //noinspection unchecked
        entityType = (Class<E>) typeArguments[0];
        //noinspection unchecked
        keyType = (Class<K>) typeArguments[1];
    }

    @Override
    public Class<R> getRepositoryType() {
        return repositoryType;
    }

    @Override
    public Map<K, E> getData() {
        return new HashMap<K, E>(data);
    }

    @Override
    public Map<MethodDescriptor, DataOperation<E, K>> getOperations() {
        return new HashMap<MethodDescriptor, DataOperation<E, K>>(operations);
    }

    @Override
    public String getKeyProperty() {
        return keyProperty;
    }

    @Override
    public KeyGeneration getKeyGeneration() {
        return keyGeneration;
    }

    @Override
    public Class<E> getEntityType() {
        return entityType;
    }

    @Override
    public Class<K> getKeyType() {
        return keyType;
    }

}
