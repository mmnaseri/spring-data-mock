package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class VoidResultAdapter extends AbstractResultAdapter<Object> {

    public VoidResultAdapter() {
        super(Integer.MIN_VALUE);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return void.class.equals(invocation.getMethod().getReturnType());
    }

    @Override
    public Object adapt(Invocation invocation, Object originalValue) {
        return null;
    }

}
