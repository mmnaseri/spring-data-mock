package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public class ImmutableRepositoryMetadata implements RepositoryMetadata {

    private final Class<? extends Serializable> identifierType;
    private final Class<?> entityType;
    private final Class<?> repositoryInterface;

    public ImmutableRepositoryMetadata(Class<? extends Serializable> identifierType, Class<?> entityType, Class<?> repositoryInterface) {
        this.identifierType = identifierType;
        this.entityType = entityType;
        this.repositoryInterface = repositoryInterface;
    }

    @Override
    public Class<? extends Serializable> getIdentifierType() {
        return identifierType;
    }

    @Override
    public Class<?> getEntityType() {
        return entityType;
    }

    @Override
    public Class<?> getRepositoryInterface() {
        return repositoryInterface;
    }

}
