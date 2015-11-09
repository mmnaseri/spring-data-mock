package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import org.springframework.core.GenericTypeResolver;
import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class AssignableRepositoryMetadataResolver extends AbstractRepositoryMetadataResolver {

    @Override
    protected RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface) {
        if (!Repository.class.isAssignableFrom(repositoryInterface)) {
            throw new IllegalArgumentException("Expected interface to extend " + Repository.class);
        }
        final Class<?>[] arguments = GenericTypeResolver.resolveTypeArguments(repositoryInterface, Repository.class);
        final Class<?> entityType = arguments[0];
        final Class<?> rawIdType = arguments[1];
        final Class<? extends Serializable> idType = rawIdType.asSubclass(Serializable.class);
        final String idProperty = resolveIdProperty(entityType, idType);
        return new ImmutableRepositoryMetadata(idType, entityType, repositoryInterface, idProperty);
    }

}
