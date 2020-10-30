package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

import java.util.List;

/**
 * This interface represents all the pieces of information that were used to instantiate a
 * repository instance.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public interface RepositoryConfiguration {

  /** @return the repository metadata */
  RepositoryMetadata getRepositoryMetadata();

  /** @return the key generator */
  KeyGenerator<?> getKeyGenerator();

  /**
   * @return implementations bound to the repository instance (including the ones derived from the
   *     {@link TypeMappingContext} used when instantiating the repository.
   */
  List<Class<?>> getBoundImplementations();
}
