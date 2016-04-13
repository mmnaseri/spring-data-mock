package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
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
