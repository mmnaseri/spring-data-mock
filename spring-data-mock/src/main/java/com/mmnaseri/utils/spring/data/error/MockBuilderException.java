package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class MockBuilderException extends RepositoryMockException {

  public MockBuilderException(String message, Throwable cause) {
    super(message, cause);
  }
}
