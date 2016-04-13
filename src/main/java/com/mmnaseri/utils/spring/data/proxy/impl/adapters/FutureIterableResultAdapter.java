package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public class FutureIterableResultAdapter extends AbstractIterableResultAdapter<Future> {

    public FutureIterableResultAdapter() {
        super(-100);
    }

    @Override
    protected Future doAdapt(Invocation invocation, final Iterable iterable) {
        //noinspection unchecked
        final FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return iterable;
            }
        });
        task.run();
        return task;
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && originalValue instanceof Iterable && invocation.getMethod().getReturnType().equals(Future.class);
    }

}
