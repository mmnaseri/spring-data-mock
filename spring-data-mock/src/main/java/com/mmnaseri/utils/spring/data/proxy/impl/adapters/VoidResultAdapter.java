package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * This adapter is used to adapt any value to the method type of {@literal void} by returning
 * {@literal null}.
 *
 * <p>This adapter is executed at {@link Integer#MIN_VALUE the lowest possible priority}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class VoidResultAdapter extends AbstractResultAdapter<Object> {

  public VoidResultAdapter() {
    super(Integer.MIN_VALUE);
  }

  @Override
  public boolean accepts(Invocation invocation, Object originalValue) {
    return void.class.equals(invocation.getMethod().getReturnType());
  }

  @Override
  public Object adapt(Invocation invocation, Object originalValue) {
    return null;
  }
}
