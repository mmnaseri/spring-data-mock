package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureTask;

import java.util.concurrent.Callable;

/**
 * <p>This class will adapt results from an iterable object to a <em>listenable</em> future. The future task returned
 * will have already
 * executed with the results available.</p>
 *
 * <p>It will accept adaptations wherein the original value is some sort of iterable and the required return type
 * is an instance of {@link ListenableFuture}. Remember that it does <em>not</em> check for individual object type
 * compatibility.</p>
 *
 * <p>This adapter will execute at priority {@literal -50}.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class ListenableFutureIterableResultAdapter extends AbstractIterableResultAdapter<ListenableFuture> {

    public ListenableFutureIterableResultAdapter() {
        super(-50);
    }

    @Override
    protected ListenableFuture doAdapt(Invocation invocation, final Iterable iterable) {
        final ListenableFutureTask task = new ListenableFutureTask<>((Callable<Object>) () -> iterable);
        task.run();
        return task;
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue instanceof Iterable && invocation.getMethod().getReturnType()
                                                              .equals(ListenableFuture.class);
    }

}
