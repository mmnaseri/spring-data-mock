package com.mmnaseri.utils.spring.data.proxy.impl.regular;

import com.mmnaseri.utils.spring.data.error.DataOperationExecutionException;
import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;

/**
 * An invocation handler that attempts to locate and call an appropriate default method from an interface type.
 */
public class InterfaceDefaultMethodNonDataOperation implements NonDataOperationHandler {

  @Override
  public boolean handles(Object proxy, Method method, Object... args) {
    return method.isDefault();
  }

  @Override
  public Object invoke(Object proxy, Method method, Object... args) {
    try {
      return callDefaultMethod(new MethodInvocation(proxy, method, args));
    } catch (Throwable throwable) {
      throw new DataOperationExecutionException("Failed to execute default method " + method, throwable);
    }
  }


  private Object callDefaultMethod(final MethodInvocation invocation) throws Throwable {
    try {
      return callDefaultMethodWithLookup(defaultLookup(), invocation);
    } catch (IllegalAccessException e) {
      if (e.getMessage().matches(".*no private access for invokespecial.*")) {
        return tryWithAccessibleLookup(invocation);
      }
      throw e;
    }
  }

  private Object tryWithAccessibleLookup(final MethodInvocation invocation) throws Throwable {
    try {
      return callDefaultMethodWithLookup(accessibleLookup(invocation), invocation);
    } catch (Throwable throwable) {
      if (throwable.getClass().getName().equals("java.lang.reflect.InaccessibleObjectException")) {
        return tryWithPrivateLookup(invocation);
      }
      throw throwable;
    }
  }

  private Object tryWithPrivateLookup(final MethodInvocation invocation) throws Throwable {
    return callDefaultMethodWithLookup(privateLookup(invocation), invocation);
  }

  private MethodHandles.Lookup privateLookup(final MethodInvocation invocation)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
    Method privateLookupIn =
      MethodHandles.class.getDeclaredMethod(
        "privateLookupIn", Class.class, MethodHandles.Lookup.class);
    return (MethodHandles.Lookup)
             privateLookupIn.invoke(
               null, invocation.method().getDeclaringClass(), MethodHandles.lookup());
  }

  private Object callDefaultMethodWithLookup(
    MethodHandles.Lookup lookup, MethodInvocation invocation) throws Throwable {
    try {
      return lookup
               .in(invocation.method().getDeclaringClass())
               .unreflectSpecial(invocation.method(), invocation.instance().getClass())
               .bindTo(invocation.instance())
               .invokeWithArguments(invocation.arguments());
    } catch (IllegalAccessException exception) {
      if (exception.getMessage().contains("no private access for invokespecial")) {
        return lookup
                 .findSpecial(
                   invocation.method().getDeclaringClass(),
                   invocation.method().getName(),
                   MethodType.methodType(
                     invocation.method().getReturnType(), invocation.method().getParameterTypes()),
                   invocation.method().getDeclaringClass())
                 .bindTo(invocation.instance())
                 .invokeWithArguments(invocation.arguments());
      } else {
        throw exception;
      }
    }
  }

  private static MethodHandles.Lookup defaultLookup() {
    return MethodHandles.lookup();
  }

  private static MethodHandles.Lookup accessibleLookup(MethodInvocation invocation)
    throws NoSuchMethodException, IllegalAccessException, InvocationTargetException,
           InstantiationException {
    Constructor<MethodHandles.Lookup> constructor =
      MethodHandles.Lookup.class.getDeclaredConstructor(Class.class);
    constructor.setAccessible(true);
    return constructor.newInstance(invocation.method().getDeclaringClass());

  }


  private static class MethodInvocation {

    private final Object instance;
    private final Method method;
    private final Object[] arguments;

    public MethodInvocation(
      final Object instance, final Method method, final Object[] arguments) {
      this.instance = instance;
      this.arguments = arguments == null ? new Object[0] : Arrays.copyOf(arguments, arguments.length);
      this.method = method;
    }

    public Object instance() {
      return instance;
    }

    public Object[] arguments() {
      return Arrays.copyOf(arguments, arguments.length);
    }

    public Method method() {
      return method;
    }

  }

}
