package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * This adapter will try to adapt a {@literal null} value to a future.
 *
 * <p>It adapts results if the return type is of type {@link Future} and the original value is
 * {@literal null}.
 *
 * <p>This adapter runs at the priority of {@literal -150}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class NullToFutureResultAdapter extends AbstractResultAdapter<Future> {

  public NullToFutureResultAdapter() {
    super(-150);
  }

  @Override
  public boolean accepts(Invocation invocation, Object originalValue) {
    return originalValue == null && invocation.getMethod().getReturnType().equals(Future.class);
  }

  @Override
  public Future adapt(Invocation invocation, Object originalValue) {
    //noinspection unchecked
    final FutureTask task = new FutureTask(() -> null);
    task.run();
    return task;
  }
}
