package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.NoOpKeyGenerator;
import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.error.MockBuilderException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * This class implements the interfaces used to define a DSL for mocking a repository.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public class RepositoryMockBuilder implements Start, ImplementationAnd, KeyGeneration {

    private final RepositoryFactory factory;
    private final List<Class<?>> implementations;
    private final KeyGenerator<? extends Serializable> keyGenerator;

    public RepositoryMockBuilder() {
        this(null, new LinkedList<Class<?>>(), null);
    }

    private RepositoryMockBuilder(RepositoryFactory factory, List<Class<?>> implementations, KeyGenerator<? extends Serializable> keyGenerator) {
        this.factory = factory;
        this.implementations = implementations;
        this.keyGenerator = keyGenerator;
    }

    @Override
    public KeyGeneration useConfiguration(RepositoryFactoryConfiguration configuration) {
        return new RepositoryMockBuilder(new DefaultRepositoryFactory(configuration), implementations, keyGenerator);
    }

    @Override
    public KeyGeneration useFactory(RepositoryFactory factory) {
        return new RepositoryMockBuilder(factory, implementations, keyGenerator);
    }

    @Override
    public ImplementationAnd usingImplementation(Class<?> implementation) {
        final LinkedList<Class<?>> implementations = new LinkedList<>(this.implementations);
        implementations.add(implementation);
        return new RepositoryMockBuilder(factory, implementations, keyGenerator);
    }

    @Override
    public ImplementationAnd and(Class<?> implementation) {
        return usingImplementation(implementation);
    }

    @Override
    public <S extends Serializable> Implementation generateKeysUsing(KeyGenerator<S> keyGenerator) {
        return new RepositoryMockBuilder(factory, implementations, keyGenerator);
    }

    @Override
    public <S extends Serializable, G extends KeyGenerator<S>> Implementation generateKeysUsing(Class<G> generatorType) {
        //noinspection unchecked
        final G instance = (G) createKeyGenerator(generatorType);
        return generateKeysUsing(instance);
    }

    @Override
    public Implementation withoutGeneratingKeys() {
        return new RepositoryMockBuilder(factory, implementations, new NoOpKeyGenerator<>());
    }

    private KeyGenerator<?> createKeyGenerator(Class<? extends KeyGenerator> generatorType) {
        final KeyGenerator instance;
        try {
            instance = generatorType.newInstance();
        } catch (Exception e) {
            throw new MockBuilderException("Failed to instantiate key generator of type " + generatorType, e);
        }
        return instance;
    }

    @Override
    public <E> E mock(Class<E> repositoryInterface) {
        final RepositoryFactory repositoryFactory;
        if (factory == null) {
            repositoryFactory = RepositoryFactoryBuilder.defaultFactory();
        } else {
            repositoryFactory = this.factory;
        }
        if (keyGenerator == null) {
            final RepositoryFactoryConfiguration configuration = repositoryFactory.getConfiguration();
            final KeyGenerator<? extends Serializable> evaluatedKeyGenerator;
            if (configuration.getDefaultKeyGenerator() != null) {
                evaluatedKeyGenerator = configuration.getDefaultKeyGenerator();
            } else {
                final KeyGeneratorProvider generatorProvider = new KeyGeneratorProvider();
                final RepositoryMetadata metadata = configuration.getRepositoryMetadataResolver().resolve(repositoryInterface);
                final Class<? extends Serializable> identifierType = metadata.getIdentifierType();
                final Class<? extends KeyGenerator<? extends Serializable>> keyGeneratorType = generatorProvider.getKeyGenerator(identifierType);
                evaluatedKeyGenerator = createKeyGenerator(keyGeneratorType);
            }
            return generateKeysUsing(evaluatedKeyGenerator).mock(repositoryInterface);
        } else {
            return repositoryFactory.getInstance(keyGenerator, repositoryInterface, implementations.toArray(new Class[implementations.size()]));
        }
    }

}
