package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;

import java.util.Iterator;
import java.util.Optional;

/**
 * This adapter accepts all invocations wherein the original value is an {@link Iterable} object and
 * the requested method type is an {@link Optional} value.
 *
 * <p>While adapting, the adapter will also check that the iterable yields only one item and that it
 * is of the same type or of a child type of the type requested by the invoked method.
 *
 * <p>This adapter runs at the priority {@literal -400}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 2.1.1 (10/29/2020)
 */
public class OptionalResultAdapter extends AbstractIterableResultAdapter<Object> {

  public OptionalResultAdapter() {
    super(-400);
  }

  @Override
  public boolean accepts(final Invocation invocation, final Object originalValue) {
    if (!(originalValue instanceof Iterable)) {
      return false;
    }
    final Class<?> returnType = invocation.getMethod().getReturnType();
    return returnType.isAssignableFrom(Optional.class);
  }

  @Override
  protected Object doAdapt(final Invocation invocation, final Iterable iterable) {
    final Iterator iterator = iterable.iterator();
    if (iterator.hasNext()) {
      final Object value = iterator.next();
      if (iterator.hasNext()) {
        throw new ResultAdapterFailureException(
            iterable,
            invocation.getMethod().getReturnType(),
            "Expected only one item but found many");
      }
      return Optional.of(value);
    } else {
      return Optional.empty();
    }
  }
}
