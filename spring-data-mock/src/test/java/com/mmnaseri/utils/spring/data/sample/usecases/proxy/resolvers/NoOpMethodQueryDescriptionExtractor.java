package com.mmnaseri.utils.spring.data.sample.usecases.proxy.resolvers;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.MethodQueryDescriptionExtractor;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;

import java.lang.reflect.Method;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 6:32 PM)
 */
public class NoOpMethodQueryDescriptionExtractor extends MethodQueryDescriptionExtractor {

    private boolean called;

    public NoOpMethodQueryDescriptionExtractor() {
        super(null);
        called = false;
    }

    @Override
    public QueryDescriptor extract(RepositoryMetadata repositoryMetadata, RepositoryFactoryConfiguration configuration,
                                   Method method) {
        called = true;
        return null;
    }

    public boolean isCalled() {
        return called;
    }
}
