package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Iterator;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/24/15)
 */
public class NullToIteratorResultAdapter extends AbstractResultAdapter<Iterator> {

    public NullToIteratorResultAdapter() {
        super(-350);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue == null && Iterator.class.equals(invocation.getMethod().getReturnType());
    }

    @Override
    public Iterator adapt(Invocation invocation, Object originalValue) {
        return new EmptyIterator();
    }

}
