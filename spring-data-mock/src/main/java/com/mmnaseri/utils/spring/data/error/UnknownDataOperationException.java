package com.mmnaseri.utils.spring.data.error;

import java.lang.reflect.Method;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class UnknownDataOperationException extends DataOperationException {

  public UnknownDataOperationException(Method method) {
    super("Failed to resolve operation for method: " + method);
  }
}
