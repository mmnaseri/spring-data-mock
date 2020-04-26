package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

/**
 * <p>This adapter will try to adapt a {@literal null} value to a listenable future.</p>
 *
 * <p>It adapts results if the return type is of type {@link ListenableFuture} and the original value is {@literal
 * null}.</p>
 *
 * <p>This adapter runs at the priority of {@literal -100}.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
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
        final ListenableFutureTask task = new ListenableFutureTask(() -> null);
        task.run();
        return task;
    }

}
