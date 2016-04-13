package com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.QueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;

import java.lang.reflect.Method;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 6:32 PM)
 */
public class NoOpQueryDescriptionExtractor extends QueryDescriptionExtractor {

    private boolean called;

    public NoOpQueryDescriptionExtractor() {
        super(null);
        called = false;
    }

    @Override
    public QueryDescriptor extract(RepositoryMetadata repositoryMetadata, Method method, RepositoryFactoryConfiguration configuration) {
        called = true;
        return null;
    }

    public boolean isCalled() {
        return called;
    }
}
