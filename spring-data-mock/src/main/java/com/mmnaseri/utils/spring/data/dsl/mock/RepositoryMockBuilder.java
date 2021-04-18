package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerationStrategy;
import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.NoOpKeyGenerator;
import com.mmnaseri.utils.spring.data.dsl.factory.RepositoryFactoryBuilder;
import com.mmnaseri.utils.spring.data.error.MockBuilderException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

/**
 * This class implements the interfaces used to define a DSL for mocking a repository.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public class RepositoryMockBuilder implements Start, ImplementationAnd, KeyGeneration {

  private final RepositoryFactory factory;
  private final List<Class<?>> implementations;
  private final KeyGenerator<?> keyGenerator;
  private final KeyGenerationStrategy keyGenerationStrategy;

  public RepositoryMockBuilder() {
    this(null, new LinkedList<>(), null, null);
  }

  private RepositoryMockBuilder(
      RepositoryFactory factory, List<Class<?>> implementations, KeyGenerator<?> keyGenerator, KeyGenerationStrategy keyGenerationStrategy) {
    this.factory = factory;
    this.implementations = implementations;
    this.keyGenerator = keyGenerator;
    this.keyGenerationStrategy = keyGenerationStrategy;
  }

  @Override
  public KeyGeneration useConfiguration(RepositoryFactoryConfiguration configuration) {
    return new RepositoryMockBuilder(
        new DefaultRepositoryFactory(configuration), implementations, keyGenerator, keyGenerationStrategy);
  }

  @Override
  public KeyGeneration useFactory(RepositoryFactory factory) {
    return new RepositoryMockBuilder(factory, implementations, keyGenerator, keyGenerationStrategy);
  }

  @Override
  public ImplementationAnd usingImplementation(Class<?> implementation) {
    final LinkedList<Class<?>> implementations = new LinkedList<>(this.implementations);
    implementations.add(implementation);
    return new RepositoryMockBuilder(factory, implementations, keyGenerator, keyGenerationStrategy);
  }

  @Override
  public ImplementationAnd and(Class<?> implementation) {
    return usingImplementation(implementation);
  }

  @Override
  public <S> Implementation generateKeysUsing(KeyGenerator<S> keyGenerator) {
    return generateKeysUsing(keyGenerator, keyGenerationStrategy);
  }

  @Override
  public <S> Implementation generateKeysUsing(KeyGenerator<S> keyGenerator, KeyGenerationStrategy keyGenerationStrategy) {
    return new RepositoryMockBuilder(factory, implementations, keyGenerator, keyGenerationStrategy);
  }

  @Override
  public <S, G extends KeyGenerator<S>> Implementation generateKeysUsing(Class<G> generatorType) {
    return generateKeysUsing(generatorType, keyGenerationStrategy);
  }

  @Override
  public <S, G extends KeyGenerator<S>> Implementation generateKeysUsing(Class<G> generatorType, KeyGenerationStrategy keyGenerationStrategy) {
    //noinspection unchecked
    final G instance = (G) createKeyGenerator(generatorType);
    return generateKeysUsing(instance, keyGenerationStrategy);
  }

  @Override
  public Implementation withoutGeneratingKeys() {
    return new RepositoryMockBuilder(factory, implementations, new NoOpKeyGenerator<>(), keyGenerationStrategy);
  }

  private KeyGenerator<?> createKeyGenerator(Class<? extends KeyGenerator> generatorType) {
    final KeyGenerator instance;
    try {
      instance = generatorType.getDeclaredConstructor().newInstance();
    } catch (Exception e) {
      throw new MockBuilderException(
          "Failed to instantiate key generator of type " + generatorType, e);
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
      final KeyGenerator<?> evaluatedKeyGenerator;
      if (configuration.getDefaultKeyGenerator() != null) {
        evaluatedKeyGenerator = configuration.getDefaultKeyGenerator();
      } else {
        final KeyGeneratorProvider generatorProvider = new KeyGeneratorProvider();
        final RepositoryMetadata metadata =
            configuration.getRepositoryMetadataResolver().resolve(repositoryInterface);
        final Class<?> identifierType = metadata.getIdentifierType();
        final Class<? extends KeyGenerator<?>> keyGeneratorType =
            generatorProvider.getKeyGenerator(identifierType);
        evaluatedKeyGenerator = createKeyGenerator(keyGeneratorType);
      }
      return generateKeysUsing(evaluatedKeyGenerator, keyGenerationStrategy).mock(repositoryInterface);
    } else {
      return repositoryFactory.getInstance(
          keyGenerator, keyGenerationStrategy, repositoryInterface, implementations.toArray(new Class[0]));
    }
  }
}
