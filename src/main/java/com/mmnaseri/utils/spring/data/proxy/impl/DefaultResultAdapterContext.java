package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;
import com.mmnaseri.utils.spring.data.proxy.impl.adapters.*;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

/**
 * This is the default implementation for registering and containing result adapters.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/24/15)
 */
@SuppressWarnings("WeakerAccess")
public class DefaultResultAdapterContext implements ResultAdapterContext {

    private final List<ResultAdapter<?>> adapters;

    /**
     * Instantiates the context and registers all the default adapters.
     */
    public DefaultResultAdapterContext() {
        this(true);
    }

    /**
     * Instantiates the context
     * @param registerDefaults    whether default adapters should be registered by default.
     */
    public DefaultResultAdapterContext(boolean registerDefaults) {
        adapters = new ArrayList<>();
        if (registerDefaults) {
            adapters.add(new VoidResultAdapter());
            adapters.add(new SameTypeResultAdapter());
            adapters.add(new NullSimpleResultAdapter());
            adapters.add(new NullToIteratorResultAdapter());
            adapters.add(new NullToCollectionResultAdapter());
            adapters.add(new NullToIterableResultAdapter());
            adapters.add(new NullToSliceResultAdapter());
            adapters.add(new NullToFutureResultAdapter());
            adapters.add(new NullToListenableFutureResultAdapter());
            adapters.add(new NumberIterableResultAdapter());
            adapters.add(new SimpleIterableResultAdapter());
            adapters.add(new IteratorIterableResultAdapter());
            adapters.add(new CollectionIterableResultAdapter());
            adapters.add(new SliceIterableResultAdapter());
            adapters.add(new PageIterableResultAdapter());
            adapters.add(new GeoPageIterableResultAdapter());
            adapters.add(new FutureIterableResultAdapter());
            adapters.add(new ListenableFutureIterableResultAdapter());
            Collections.sort(adapters);
        }
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

    @Override
    public Collection<ResultAdapter<?>> getAdapters() {
        return Collections.unmodifiableCollection(adapters);
    }

}
