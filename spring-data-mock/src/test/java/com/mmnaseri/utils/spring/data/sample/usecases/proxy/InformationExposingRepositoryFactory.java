package com.mmnaseri.utils.spring.data.sample.usecases.proxy;

import com.mmnaseri.utils.spring.data.domain.KeyGenerationStrategy;
import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.impl.ImmutableRepositoryConfiguration;

import java.lang.reflect.Proxy;
import java.util.Arrays;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 11:10 AM)
 */
public class InformationExposingRepositoryFactory implements RepositoryFactory {

    private final RepositoryFactoryConfiguration configuration;
    private final RepositoryFactory delegate;

    public InformationExposingRepositoryFactory(RepositoryFactoryConfiguration configuration) {
        this.configuration = configuration;
        delegate = new DefaultRepositoryFactory(configuration);
    }

    @Override
    public <E> E getInstance(KeyGenerator<?> keyGenerator, KeyGenerationStrategy keyGenerationStrategy, Class<E> repositoryInterface, Class... implementations) {
        final E instance = delegate.getInstance(keyGenerator, keyGenerationStrategy, repositoryInterface, implementations);
        //noinspection unchecked
        final RepositoryConfiguration configuration = new ImmutableRepositoryConfiguration(
                this.configuration.getRepositoryMetadataResolver().resolve(repositoryInterface), keyGenerator,
                Arrays.asList(implementations));
        final DefaultInformationExposingRepository implementation = new DefaultInformationExposingRepository(
                configuration, getConfiguration());
        final InformationExposingInvocationHandler<E> handler = new InformationExposingInvocationHandler<>(
                implementation, instance);
        final Object proxyInstance = Proxy.newProxyInstance(instance.getClass().getClassLoader(), new Class[]{
                repositoryInterface,
                InformationExposingRepository.class
        }, handler);
        return repositoryInterface.cast(proxyInstance);
    }

    @Override
    public RepositoryFactoryConfiguration getConfiguration() {
        return configuration;
    }

}
