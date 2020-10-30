package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public abstract class RepositoryMockException extends RuntimeException {

  public RepositoryMockException(String message) {
    super(message);
  }

  public RepositoryMockException(String message, Throwable cause) {
    super(message, cause);
  }
}
