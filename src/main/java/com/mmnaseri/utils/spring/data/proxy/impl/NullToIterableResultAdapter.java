package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Collections;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class NullToIterableResultAdapter extends AbstractResultAdapter<Iterable> {

    public NullToIterableResultAdapter() {
        super(-250);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return invocation.getMethod().getReturnType().equals(Iterable.class) && originalValue == null;
    }

    @Override
    public Iterable adapt(Invocation invocation, Object originalValue) {
        return Collections.emptyList();
    }

}
