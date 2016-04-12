package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public abstract class AbstractIterableResultAdapter<E> extends AbstractResultAdapter<E> {

    public AbstractIterableResultAdapter(int priority) {
        super(priority);
    }

    @Override
    public E adapt(Invocation invocation, Object originalValue) {
        final Iterable iterable = (Iterable) originalValue;
        return doAdapt(invocation, iterable);
    }

    protected abstract E doAdapt(Invocation invocation, Iterable iterable);

}
