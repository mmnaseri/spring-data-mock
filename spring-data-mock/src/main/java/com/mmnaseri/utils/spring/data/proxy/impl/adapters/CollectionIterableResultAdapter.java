package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.tools.CollectionInstanceUtils;

import java.util.Collection;

/**
 * This adapter will adapt results from an iterable to the appropriate collection type. It will
 * accept adaptations if the original value is an iterable object and the target result is some sort
 * {@link java.util.Collection collection}.
 *
 * <p>This adapter will execute at priority {@literal -300}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class CollectionIterableResultAdapter extends AbstractIterableResultAdapter<Collection> {

  public CollectionIterableResultAdapter() {
    super(-300);
  }

  @Override
  protected Collection doAdapt(Invocation invocation, Iterable iterable) {
    final Collection collection;
    try {
      collection = CollectionInstanceUtils.getCollection(invocation.getMethod().getReturnType());
    } catch (IllegalArgumentException e) {
      throw new ResultAdapterFailureException(iterable, invocation.getMethod().getReturnType(), e);
    }
    for (Object item : iterable) {
      //noinspection unchecked
      collection.add(item);
    }
    return collection;
  }

  @Override
  public boolean accepts(Invocation invocation, Object originalValue) {
    return originalValue instanceof Iterable
        && Collection.class.isAssignableFrom(invocation.getMethod().getReturnType());
  }
}
