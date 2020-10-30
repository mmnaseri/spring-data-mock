package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/9/16, 12:29 AM)
 */
public interface QueryDescriptionExtractor<T> {

  QueryDescriptor extract(
      RepositoryMetadata repositoryMetadata,
      RepositoryFactoryConfiguration configuration,
      T target);
}
