package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Iterator;
import java.util.concurrent.Future;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class SimpleResultAdapter extends AbstractIterableResultAdapter<Object> {

    public SimpleResultAdapter() {
        super(-400);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        final Class<?> returnType = invocation.getMethod().getReturnType();
        return !Iterable.class.isAssignableFrom(returnType) &&
                !Iterator.class.isAssignableFrom(returnType) &&
                !Future.class.isAssignableFrom(returnType) &&
                originalValue != null;
    }

    @Override
    protected Object doAdapt(Invocation invocation, Iterable iterable) {
        final Iterator iterator = iterable.iterator();
        if (iterator.hasNext()) {
            final Object value = iterator.next();
            if (iterator.hasNext()) {
                throw new IllegalStateException("Expected to get only one item but got many");
            }
            if (!invocation.getMethod().getReturnType().isInstance(value)) {
                throw new IllegalStateException("Expected a value of type " + invocation.getMethod().getReturnType());
            }
            return value;
        } else {
            return null;
        }
    }

}
