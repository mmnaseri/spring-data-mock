package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class DataFunctionException extends DataOperationException {

  public DataFunctionException(String message) {
    super(message);
  }

  public DataFunctionException(String message, Throwable cause) {
    super(message, cause);
  }
}
