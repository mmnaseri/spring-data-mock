package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.AbstractRepositoryMetadataResolver;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 7:03 PM)
 */
public class NullReturningRepositoryMetadataResolver extends AbstractRepositoryMetadataResolver {

    private Class<?> repositoryInterface;

    @Override
    protected RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface) {
        this.repositoryInterface = repositoryInterface;
        return null;
    }

    public Class<?> getRepositoryInterface() {
        return repositoryInterface;
    }

}
