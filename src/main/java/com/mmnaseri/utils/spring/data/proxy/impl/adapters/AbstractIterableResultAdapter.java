package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * This is the base class that provides the basics for adapting results from an iterable object.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
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

    /**
     * This is called when we want to adapt an iterable object to another type.
     * @param invocation    the invocation which called for this adaptation
     * @param iterable      the iterable to be adapted to the appropriate result type
     * @return the adapted result
     */
    protected abstract E doAdapt(Invocation invocation, Iterable iterable);

}
