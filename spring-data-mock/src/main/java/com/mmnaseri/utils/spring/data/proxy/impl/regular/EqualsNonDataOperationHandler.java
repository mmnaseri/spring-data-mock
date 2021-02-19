package com.mmnaseri.utils.spring.data.proxy.impl.regular;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.util.stream.Stream;

import static java.util.Arrays.stream;
import static java.util.stream.Collectors.toSet;

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
    if (that == null || !Proxy.isProxyClass(that.getClass())) {
      return false;
    }
    return superInterfaces(proxy.getClass()).collect(toSet()).equals(superInterfaces(that.getClass()).collect(toSet()));
  }

  static Stream<Class<?>> superInterfaces(Class<?> type) {
    return Stream.concat(Stream.of(type), stream(type.getInterfaces()).flatMap(EqualsNonDataOperationHandler::superInterfaces)).filter(item -> Modifier.isInterface(item.getModifiers()));
  }

}
