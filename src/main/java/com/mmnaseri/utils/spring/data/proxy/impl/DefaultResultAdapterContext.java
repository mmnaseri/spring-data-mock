package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.impl.adapters.*;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class DefaultResultAdapterContext implements ResultAdapterContext {

    private final List<ResultAdapter<?>> adapters = new ArrayList<ResultAdapter<?>>();

    public DefaultResultAdapterContext() {
        adapters.add(new VoidResultAdapter());
        adapters.add(new SameTypeResultAdapter());
        adapters.add(new NullSimpleResultAdapter());
        adapters.add(new NullToIteratorResultAdapter());
        adapters.add(new NullToCollectionResultAdapter());
        adapters.add(new NullToIterableResultAdapter());
        adapters.add(new NullToSliceResultAdapter());
        adapters.add(new NullToFutureResultAdapter());
        adapters.add(new NullToListenableFutureResultAdapter());
        adapters.add(new IterableNumberResultAdapter());
        adapters.add(new SimpleResultAdapter());
        adapters.add(new IteratorResultAdapter());
        adapters.add(new CollectionResultAdapter());
        adapters.add(new IterableSliceResultAdapter());
        adapters.add(new PageResultAdapter());
        adapters.add(new GeoPageResultAdapter());
        adapters.add(new FutureResultAdapter());
        adapters.add(new ListenableFutureResultAdapter());
        Collections.sort(adapters);
        this.adapters.addAll(adapters);
    }

    @Override
    public synchronized void register(ResultAdapter<?> adapter) {
        adapters.add(adapter);
        Collections.sort(adapters);
    }

    @Override
    public Object adapt(Invocation invocation, Object originalResult) {
        for (ResultAdapter<?> adapter : adapters) {
            if (adapter.accepts(invocation, originalResult)) {
                return adapter.adapt(invocation, originalResult);
            }
        }
        throw new ResultAdapterFailureException(originalResult, invocation.getMethod().getReturnType());
    }

}
