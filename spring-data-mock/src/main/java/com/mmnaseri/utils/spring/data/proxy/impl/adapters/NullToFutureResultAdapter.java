package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * <p>This adapter will try to adapt a {@literal null} value to a future.</p>
 *
 * <p>It adapts results if the return type is of type {@link Future} and the original value is {@literal null}.</p>
 *
 * <p>This adapter runs at the priority of {@literal -150}.</p>
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
        final FutureTask task = new FutureTask(new Callable() {

            @Override
            public Object call() throws Exception {
                return null;
            }
        });
        task.run();
        return task;
    }

}
