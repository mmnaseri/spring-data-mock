package com.mmnaseri.utils.spring.data.domain;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/23/15)
 */
public interface RepositoryMetadataResolver {

    RepositoryMetadata resolve(Class<?> repositoryInterface);

}
