package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public abstract class FunctionRegistryException extends RepositoryMockException {

  public FunctionRegistryException(String message) {
    super(message);
  }
}
