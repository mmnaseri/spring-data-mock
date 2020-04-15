package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

/**
 * This is an immutable repository metadata.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/19/15)
 */
public class ImmutableRepositoryMetadata implements RepositoryMetadata {

    private final Class<?> identifierType;
    private final Class<?> entityType;
    private final Class<?> repositoryInterface;
    private final String identifier;

    public ImmutableRepositoryMetadata(Class<?> identifierType, Class<?> entityType, Class<?> repositoryInterface, String identifier) {
        this.identifierType = identifierType;
        this.entityType = entityType;
        this.repositoryInterface = repositoryInterface;
        this.identifier = identifier;
    }

    @Override
    public String getIdentifierProperty() {
        return identifier;
    }

    @Override
    public Class<?> getIdentifierType() {
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
