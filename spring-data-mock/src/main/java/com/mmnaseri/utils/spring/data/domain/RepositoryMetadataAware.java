package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface is used to inject {@link RepositoryMetadata the repository metadata} into a concrete class aiming to
 * provide method mapping for a repository.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface RepositoryMetadataAware {

    void setRepositoryMetadata(RepositoryMetadata repositoryMetadata);

}
