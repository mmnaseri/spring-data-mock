package com.mmnaseri.utils.spring.data.dsl.factory;

/**
 * Lets us register additional mappings
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface MappingContextAnd extends OperationHandlers {

  /**
   * Registers an additional mapping
   *
   * @param superType the super type for the interface
   * @param implementation the concrete class providing mapped method implementations
   * @return the rest of the configuration
   */
  MappingContextAnd and(Class<?> superType, Class<?> implementation);
}
