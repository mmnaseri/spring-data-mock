package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class RepositoryDefinitionException extends RepositoryMockException {

  public RepositoryDefinitionException(Class<?> repositoryInterface, String message) {
    super(repositoryInterface + ": " + message);
  }

  public RepositoryDefinitionException(
      Class<?> repositoryInterface, String message, Throwable cause) {
    super(repositoryInterface + ": " + message, cause);
  }
}
