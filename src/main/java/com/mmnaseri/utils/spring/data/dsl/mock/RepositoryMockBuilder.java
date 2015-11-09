package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.KeyGeneratorProvider;
import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public class RepositoryMockBuilder implements Start, ImplementationAnd, KeyGeneration {

    private static final KeyGenerator<? extends Serializable> NOOP = new KeyGenerator<Serializable>() {
        @Override
        public Serializable generate() {
            return null;
        }
    };
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
        final LinkedList<Class<?>> implementations = new LinkedList<Class<?>>(this.implementations);
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
        return new RepositoryMockBuilder(factory, implementations, instance);
    }

    @Override
    public Implementation withoutGeneratingKeys() {
        return new RepositoryMockBuilder(factory, implementations, NOOP);
    }

    protected KeyGenerator<?> createKeyGenerator(Class<? extends KeyGenerator> generatorType) {
        final KeyGenerator instance;
        try {
            instance = generatorType.newInstance();
        } catch (Exception e) {
            throw new IllegalStateException("Failed to instantiate key generator of type " + generatorType, e);
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
            final KeyGeneratorProvider generatorProvider = new KeyGeneratorProvider();
            final RepositoryMetadata metadata = repositoryFactory.getConfiguration().getRepositoryMetadataResolver().resolve(repositoryInterface);
            final Class<? extends Serializable> identifierType = metadata.getIdentifierType();
            final Class<? extends KeyGenerator<? extends Serializable>> keyGeneratorType = generatorProvider.getKeyGenerator(identifierType);
            final KeyGenerator<? extends Serializable> keyGenerator = createKeyGenerator(keyGeneratorType);
            return generateKeysUsing(keyGenerator).mock(repositoryInterface);
        } else {
            return repositoryFactory.getInstance(NOOP.equals(keyGenerator) ? null : keyGenerator, repositoryInterface, implementations.toArray(new Class[implementations.size()]));
        }
    }

}
