package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class DefaultRepositoryMetadataResolver extends AbstractRepositoryMetadataResolver {

    private final AssignableRepositoryMetadataResolver assignableRepositoryMetadataResolver = new AssignableRepositoryMetadataResolver();
    private final AnnotationRepositoryMetadataResolver annotationRepositoryMetadataResolver = new AnnotationRepositoryMetadataResolver();

    @Override
    protected RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface) {
        if (repositoryInterface.isAnnotationPresent(RepositoryDefinition.class)) {
            return annotationRepositoryMetadataResolver.resolve(repositoryInterface);
        }
        return assignableRepositoryMetadataResolver.resolve(repositoryInterface);
    }

}
