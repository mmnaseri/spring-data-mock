package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class FutureResultAdapter extends AbstractIterableResultAdapter<Future> {

    public FutureResultAdapter() {
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
        return originalValue != null && invocation.getMethod().getReturnType().equals(Future.class);
    }

}
