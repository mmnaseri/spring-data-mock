package com.mmnaseri.utils.spring.data.proxy.impl.regular;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

import java.lang.reflect.Method;

import static com.mmnaseri.utils.spring.data.proxy.impl.regular.EqualsNonDataOperationHandler.superInterfaces;
import static java.util.Comparator.comparing;
import static java.util.stream.Collectors.toList;

/**
 * This class will handle the {@link Object#toString()} method.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class ToStringNonDataOperationHandler implements NonDataOperationHandler {

  private static final String TO_STRING = "toString";

  @Override
  public boolean handles(Object proxy, Method method, Object... args) {
    return Object.class.equals(method.getDeclaringClass()) && TO_STRING.equals(method.getName());
  }

  @Override
  public Object invoke(Object proxy, Method method, Object... args) {
    return "mock<" + superInterfaces(proxy.getClass()).sorted(comparing(Class::getCanonicalName)).collect(toList()).toString() + ">";
  }
}
