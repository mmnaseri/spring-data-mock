package com.mmnaseri.utils.spring.data.proxy.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.KeyGeneratorProvider;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.dsl.config.RepositoryFactoryConfigurationBuilder;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/9/15)
 */
public class RepositoryMockBuilder implements KeyGenerationConfigurer, ImplementationConfigurer, ImplementationConfigurerConjunction {
    
    private final RepositoryFactory factory;
    private final KeyGenerator<? extends Serializable> keyGenerator;
    private final List<Class<?>> implementations;

    public static KeyGenerationConfigurer given(RepositoryFactoryConfiguration configuration) {
        return new RepositoryMockBuilder(new DefaultRepositoryFactory(configuration), null, new LinkedList<Class<?>>());
    }

    public static <S extends Serializable> ImplementationConfigurer generatingKeysUsing(KeyGenerator<S> generator) {
        return given(RepositoryFactoryConfigurationBuilder.defaultConfiguration()).generateKeysUsing(generator);
    }

    public static <S extends Serializable, G extends KeyGenerator<S>> ImplementationConfigurer generatingKeysUsing(Class<G> generatorType) {
        return given(RepositoryFactoryConfigurationBuilder.defaultConfiguration()).generateKeysUsing(generatorType);
    }

    public static <R> R mock(Class<R> repositoryInterface) {
        return generatingKeysUsing((KeyGenerator) null).instantiate(repositoryInterface);
    }

    private RepositoryMockBuilder(RepositoryFactory factory, KeyGenerator<? extends Serializable> keyGenerator, List<Class<?>> implementations) {
        this.factory = factory;
        this.keyGenerator = keyGenerator;
        this.implementations = implementations;
    }

    @Override
    public <S extends Serializable> ImplementationConfigurer generateKeysUsing(KeyGenerator<S> generator) {
        return new RepositoryMockBuilder(factory, generator, implementations);
    }

    @Override
    public <S extends Serializable, G extends KeyGenerator<S>> ImplementationConfigurer generateKeysUsing(Class<G> generatorType) {
        final G keyGenerator;
        try {
            keyGenerator = generatorType.newInstance();
        } catch (InstantiationException e) {
            throw new IllegalStateException("Failed to instantiate key generator of type " + generatorType);
        } catch (IllegalAccessException e) {
            throw new IllegalStateException("Could not access the constructor for key generator of type " + generatorType);
        }
        return generateKeysUsing(keyGenerator);
    }

    @Override
    public ImplementationConfigurerConjunction useImplementation(Class<?> implementation) {
        final LinkedList<Class<?>> implementations = new LinkedList<Class<?>>();
        implementations.add(implementation);
        return new RepositoryMockBuilder(factory, keyGenerator, implementations);
    }

    @Override
    public ImplementationConfigurerConjunction and(Class<?> implementation) {
        final LinkedList<Class<?>> implementations = new LinkedList<Class<?>>(this.implementations);
        implementations.add(implementation);
        return new RepositoryMockBuilder(factory, keyGenerator, implementations);
    }

    @Override
    public <E> E instantiate(Class<E> repositoryInterface) {
        KeyGenerator<? extends Serializable> keyGenerator = this.keyGenerator;
        if (keyGenerator == null) {
            final RepositoryFactoryConfiguration configuration = factory.getConfiguration();
            final RepositoryMetadata metadata = configuration.getRepositoryMetadataResolver().resolve(repositoryInterface);
            final KeyGeneratorProvider provider = new KeyGeneratorProvider();
            //noinspection unchecked
            final Class<? extends KeyGenerator<Serializable>> keyGeneratorType = provider.getKeyGenerator((Class<Serializable>) metadata.getIdentifierType());
            if (keyGeneratorType == null) {
                throw new IllegalStateException("Could not find a default key generator for keys of type " + metadata.getIdentifierType());
            }
            return new RepositoryMockBuilder(factory, null, implementations).generateKeysUsing(keyGeneratorType).instantiate(repositoryInterface);
        }
        return factory.getInstance(keyGenerator, repositoryInterface, implementations.toArray(new Class[implementations.size()]));
    }

}
