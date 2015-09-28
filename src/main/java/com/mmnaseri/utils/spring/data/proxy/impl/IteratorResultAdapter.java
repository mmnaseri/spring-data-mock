package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Iterator;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class IteratorResultAdapter extends AbstractIterableResultAdapter<Iterator> {

    public IteratorResultAdapter() {
        super(-350);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && Iterator.class.equals(invocation.getMethod().getReturnType());
    }

    @Override
    protected Iterator doAdapt(Invocation invocation, Iterable iterable) {
        return iterable.iterator();
    }

}
