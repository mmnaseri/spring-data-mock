package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;

/**
 * This interface allows us to set what resolver is used for resolving repository
 * metadata
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface MetadataResolver extends QueryDescriptionConfigurer {

    /**
     * Tells the builder to use the given repository metadata resolver instead of the default it has
     * @param metadataResolver    the resolver to use
     * @return the rest of the configuration
     */
    QueryDescriptionConfigurer resolveMetadataUsing(RepositoryMetadataResolver metadataResolver);

}
