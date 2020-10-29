package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Iterator;

/**
 * This adapter will try to adapt a {@literal null} value to an iterator.
 *
 * <p>It adapts results if the return type is of type {@link Iterator} and the original value is
 * {@literal null}.
 *
 * <p>This adapter runs at the priority of {@literal -350}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class NullToIteratorResultAdapter extends AbstractResultAdapter<Iterator> {

  public NullToIteratorResultAdapter() {
    super(-350);
  }

  @Override
  public boolean accepts(Invocation invocation, Object originalValue) {
    return originalValue == null && Iterator.class.equals(invocation.getMethod().getReturnType());
  }

  @Override
  public Iterator adapt(Invocation invocation, Object originalValue) {
    return EmptyIterator.INSTANCE;
  }
}
