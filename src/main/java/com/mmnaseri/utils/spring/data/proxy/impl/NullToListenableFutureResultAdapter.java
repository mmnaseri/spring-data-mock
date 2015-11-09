package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.Callable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class NullToListenableFutureResultAdapter extends AbstractResultAdapter<ListenableFuture> {

    public NullToListenableFutureResultAdapter() {
        super(-100);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue == null && invocation.getMethod().getReturnType().equals(ListenableFuture.class);
    }

    @Override
    public ListenableFuture adapt(Invocation invocation, Object originalValue) {
        //noinspection unchecked
        return new ListenableFutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
    }

}
