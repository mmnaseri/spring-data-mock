package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface MetadataResolver extends QueryDescriptionConfigurer {

    QueryDescriptionConfigurer resolveMetadataUsing(RepositoryMetadataResolver metadataResolver);

}
