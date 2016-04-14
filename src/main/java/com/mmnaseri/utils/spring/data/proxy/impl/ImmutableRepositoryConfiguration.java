package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;

import java.util.List;

/**
 * <p>This class is an immutable repository configuration.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/12/15)
 */
public class ImmutableRepositoryConfiguration implements RepositoryConfiguration {

    private final RepositoryMetadata repositoryMetadata;
    private final KeyGenerator<?> keyGenerator;
    private final List<Class<?>> boundImplementations;

    public ImmutableRepositoryConfiguration(RepositoryMetadata repositoryMetadata, KeyGenerator<?> keyGenerator, List<Class<?>> boundImplementations) {
        this.repositoryMetadata = repositoryMetadata;
        this.keyGenerator = keyGenerator;
        this.boundImplementations = boundImplementations;
    }

    @Override
    public RepositoryMetadata getRepositoryMetadata() {
        return repositoryMetadata;
    }

    @Override
    public KeyGenerator<?> getKeyGenerator() {
        return keyGenerator;
    }

    @Override
    public List<Class<?>> getBoundImplementations() {
        return boundImplementations;
    }

}
