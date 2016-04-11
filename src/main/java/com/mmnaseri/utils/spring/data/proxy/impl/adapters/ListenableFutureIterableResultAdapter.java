package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.util.concurrent.*;

import java.util.concurrent.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class ListenableFutureIterableResultAdapter extends AbstractIterableResultAdapter<ListenableFuture> {

    public ListenableFutureIterableResultAdapter() {
        super(-50);
    }

    @Override
    protected ListenableFuture doAdapt(Invocation invocation, final Iterable iterable) {
        final ExecutorService service = Executors.newSingleThreadExecutor();
        final ListenableFutureTask task = new ListenableFutureTask<>(new Callable<Object>() {
            @Override
            public Object call() throws Exception {
                return iterable;
            }
        });
        service.execute(task);
        //noinspection unchecked
        return task;
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && originalValue instanceof Iterable && invocation.getMethod().getReturnType().equals(ListenableFuture.class);
    }

}
