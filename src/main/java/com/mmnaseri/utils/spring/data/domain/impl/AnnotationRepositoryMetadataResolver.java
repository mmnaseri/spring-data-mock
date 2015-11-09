package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import org.springframework.data.repository.RepositoryDefinition;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class AnnotationRepositoryMetadataResolver extends AbstractRepositoryMetadataResolver {

    @Override
    protected RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface) {
        final RepositoryDefinition definition = repositoryInterface.getAnnotation(RepositoryDefinition.class);
        if (definition == null) {
            throw new IllegalArgumentException("Expected the repository to be annotated with @RepositoryDefinition");
        }
        final Class<?> entityType = definition.domainClass();
        final Class<? extends Serializable> idType = definition.idClass();
        String idProperty = resolveIdProperty(entityType, idType);
        return new ImmutableRepositoryMetadata(idType, entityType, repositoryInterface, idProperty);
    }

}
