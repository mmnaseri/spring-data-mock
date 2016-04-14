package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * <p>This resolver will combine generic based and annotation based metadata resolution and deliver both in a single package.</p>
 *
 * <p>This is the order in which the resolution will take place:</p>
 *
 * <ol>
 *     <li>It will first try to determine the definition by looking at {@link AnnotationRepositoryMetadataResolver annotations}.</li>
 *     <li>It will then try to resolve the metadata using {@link AssignableRepositoryMetadataResolver inheritance}.</li>
 * </ol>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
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
