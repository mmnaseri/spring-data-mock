package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * This class will try to resolve metadata from a repository interface that has been annotated with Spring's {@link
 * RepositoryDefinition @RepositoryDefinition}. If the annotation is not found, it will throw a {@link
 * RepositoryDefinitionException RepositoryDefinitionException}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class AnnotationRepositoryMetadataResolver extends AbstractRepositoryMetadataResolver {

    @Override
    protected RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface) {
        final RepositoryDefinition definition = repositoryInterface.getAnnotation(RepositoryDefinition.class);
        if (definition == null) {
            throw new RepositoryDefinitionException(repositoryInterface,
                                                    "Expected the repository to be annotated with " 
                                                            + "@RepositoryDefinition");
        }
        final Class<?> entityType = definition.domainClass();
        final Class<?> idType = definition.idClass();
        String idProperty = resolveIdProperty(entityType, idType);
        return new ImmutableRepositoryMetadata(idType, entityType, repositoryInterface, idProperty);
    }

}
