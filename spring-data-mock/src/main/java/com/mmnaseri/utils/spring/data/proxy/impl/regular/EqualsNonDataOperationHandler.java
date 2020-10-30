package com.mmnaseri.utils.spring.data.proxy.impl.regular;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

import java.lang.reflect.Method;

/**
 * This class will handle the {@link Object#equals(Object)} method.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class EqualsNonDataOperationHandler implements NonDataOperationHandler {

  private static final String EQUALS = "equals";

  @Override
  public boolean handles(Object proxy, Method method, Object... args) {
    return Object.class.equals(method.getDeclaringClass()) && EQUALS.equals(method.getName());
  }

  @Override
  public Object invoke(Object proxy, Object... args) {
    final Object that = args[0];
    return proxy.equals(that);
  }
}
