package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.util.Iterator;
import java.util.concurrent.Future;

/**
 * This adapter accepts all invocations wherein the original value is an {@link Iterable} object and
 * the requested method type is a simple value. Simple types are types that are not a subtype of
 * {@link Iterable}, {@link Iterator}, or {@link Future}.
 *
 * <p>While adapting, the adapter will also check that the iterable yields only one item and that it
 * is of the same type or of a child type of the type requested by the invoked method.
 *
 * <p>This adapter runs at the priority {@literal -400}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class SimpleIterableResultAdapter extends AbstractIterableResultAdapter<Object> {

  public SimpleIterableResultAdapter() {
    super(-400);
  }

  @Override
  public boolean accepts(Invocation invocation, Object originalValue) {
    if (!(originalValue instanceof Iterable)) {
      return false;
    }
    final Class<?> returnType = invocation.getMethod().getReturnType();
    return !Iterable.class.isAssignableFrom(returnType)
        && !Iterator.class.isAssignableFrom(returnType)
        && !Future.class.isAssignableFrom(returnType);
  }

  @Override
  protected Object doAdapt(Invocation invocation, Iterable iterable) {
    final Iterator iterator = iterable.iterator();
    if (iterator.hasNext()) {
      final Object value = iterator.next();
      if (iterator.hasNext()) {
        throw new ResultAdapterFailureException(
            iterable,
            invocation.getMethod().getReturnType(),
            "Expected only one item but found many");
      }
      if (!PropertyUtils.getTypeOf(invocation.getMethod().getReturnType()).isInstance(value)) {
        throw new ResultAdapterFailureException(
            value,
            invocation.getMethod().getReturnType(),
            "Expected value to be of the indicated type");
      }
      return value;
    } else {
      return null;
    }
  }
}
