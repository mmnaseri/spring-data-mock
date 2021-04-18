package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface is used to inject {@link KeyGenerator the key generator} into a concrete class
 * aiming to provide method mapping for a repository.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public interface KeyGeneratorAware<S> {

  void setKeyGenerator(KeyGenerator<? extends S> keyGenerator);
  void setKeyGenerationStrategy(KeyGenerationStrategy strategy);

}
