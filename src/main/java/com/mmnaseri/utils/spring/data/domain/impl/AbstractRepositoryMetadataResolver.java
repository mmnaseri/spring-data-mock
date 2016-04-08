package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.id.EntityIdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;

import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.util.Objects;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public abstract class AbstractRepositoryMetadataResolver implements RepositoryMetadataResolver {

    private final IdPropertyResolver idPropertyResolver;

    protected AbstractRepositoryMetadataResolver() {
        idPropertyResolver = new EntityIdPropertyResolver();
    }

    @Override
    public RepositoryMetadata resolve(Class<?> repositoryInterface) {
        if (repositoryInterface == null) {
            throw new RepositoryDefinitionException(null, "Repository interface must not be null");
        }
        if (!Modifier.isInterface(repositoryInterface.getModifiers())) {
            throw new RepositoryDefinitionException(repositoryInterface, "Cannot resolve repository metadata for a class object that isn't an interface");
        }
        if (!Modifier.isPublic(repositoryInterface.getModifiers())) {
            throw new RepositoryDefinitionException(repositoryInterface, "Repository interface needs to be declared as public");
        }
        return resolveFromInterface(repositoryInterface);
    }

    protected abstract RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface);

    protected String resolveIdProperty(Class<?> entityType, Class<? extends Serializable> idType) {
        return idPropertyResolver.resolve(entityType, idType);
    }

}
