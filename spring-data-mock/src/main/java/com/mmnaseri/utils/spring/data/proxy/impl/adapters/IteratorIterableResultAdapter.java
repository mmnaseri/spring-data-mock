package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Iterator;

/**
 * <p>This class will adapt results from an iterable object to an iterator.</p>
 *
 * <p>It will accept adaptations wherein the original value is some sort of iterable and the required return type
 * is an instance of {@link Iterator}. Remember that it does <em>not</em> check for individual object type
 * compatibility.</p>
 *
 * <p>This adapter will execute at priority {@literal -350}.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class IteratorIterableResultAdapter extends AbstractIterableResultAdapter<Iterator> {

    public IteratorIterableResultAdapter() {
        super(-350);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && originalValue instanceof Iterable && Iterator.class.equals(
                invocation.getMethod().getReturnType());
    }

    @Override
    protected Iterator doAdapt(Invocation invocation, Iterable iterable) {
        return iterable.iterator();
    }

}
