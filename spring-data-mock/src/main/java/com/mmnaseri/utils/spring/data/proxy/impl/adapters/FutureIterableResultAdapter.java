package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * This class will adapt results from an iterable object to a future. The future task returned will
 * have already executed with the results available.
 *
 * <p>It will accept adaptations wherein the original value is some sort of iterable and the
 * required return type is an instance of {@link Future}. Remember that it does <em>not</em> check
 * for individual object type compatibility.
 *
 * <p>This adapter will execute at priority {@literal -100}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class FutureIterableResultAdapter extends AbstractIterableResultAdapter<Future> {

  public FutureIterableResultAdapter() {
    super(-100);
  }

  @Override
  protected Future doAdapt(Invocation invocation, final Iterable iterable) {
    //noinspection unchecked
    final FutureTask task = new FutureTask(() -> iterable);
    task.run();
    return task;
  }

  @Override
  public boolean accepts(Invocation invocation, Object originalValue) {
    return originalValue instanceof Iterable
        && invocation.getMethod().getReturnType().equals(Future.class);
  }
}
