package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Iterator;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public class IteratorIterableResultAdapter extends AbstractIterableResultAdapter<Iterator> {

    public IteratorIterableResultAdapter() {
        super(-350);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && originalValue instanceof Iterable && Iterator.class.equals(invocation.getMethod().getReturnType());
    }

    @Override
    protected Iterator doAdapt(Invocation invocation, Iterable iterable) {
        return iterable.iterator();
    }

}
