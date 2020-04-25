package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.repository.RepositoryDefinition;

/**
 * <p>This resolver will combine generic based and annotation based metadata resolution and deliver both in a single
 * package.</p>
 *
 * <p>This is the order in which the resolution will take place:</p>
 *
 * <ol>
 *     <li>It will first try to determine the definition by looking at {@link AnnotationRepositoryMetadataResolver
 *     annotations}.</li>
 *     <li>It will then try to resolve the metadata using {@link AssignableRepositoryMetadataResolver inheritance}.</li>
 * </ol>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class DefaultRepositoryMetadataResolver extends AbstractRepositoryMetadataResolver {

    private static final Log log = LogFactory.getLog(DefaultRepositoryMetadataResolver.class);
    private final AssignableRepositoryMetadataResolver assignableRepositoryMetadataResolver =
            new AssignableRepositoryMetadataResolver();
    private final AnnotationRepositoryMetadataResolver annotationRepositoryMetadataResolver =
            new AnnotationRepositoryMetadataResolver();

    @Override
    protected RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface) {
        if (repositoryInterface.isAnnotationPresent(RepositoryDefinition.class)) {
            log.info(
                    "Since the repository interface was annotated with @RepositoryDefinition we will try to resolve "
                            + "the metadata using the provided annotation");
            return annotationRepositoryMetadataResolver.resolve(repositoryInterface);
        }
        log.info(
                "Since no annotation was found on the repository, we will try to read the metadata from the generic "
                        + "type parameters derived from the Repository interface");
        return assignableRepositoryMetadataResolver.resolve(repositoryInterface);
    }

}
