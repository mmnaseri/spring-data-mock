package com.mmnaseri.utils.spring.data.domain;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public interface RepositoryMetadataResolver {

    RepositoryMetadata resolve(Class<?> repositoryInterface);

}
