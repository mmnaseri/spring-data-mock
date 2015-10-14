package com.mmnaseri.utils.spring.data.dsl.config;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public interface MetadataResolverConfigurer extends FunctionRegistryConfigurer {

    FunctionRegistryConfigurer resolvingMetadataUsing(RepositoryMetadataResolver repositoryMetadataResolver);

}
