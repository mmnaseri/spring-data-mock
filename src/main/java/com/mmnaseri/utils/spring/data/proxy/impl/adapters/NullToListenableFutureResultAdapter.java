package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
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
        final ListenableFutureTask task = new ListenableFutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return null;
            }
        });
        final ExecutorService service = Executors.newSingleThreadExecutor();
        service.submit(task);
        return task;
    }

}
