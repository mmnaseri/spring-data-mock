package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public class SameTypeResultAdapter extends AbstractResultAdapter<Object> {

    public SameTypeResultAdapter() {
        super(-500);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        return originalValue != null && invocation.getMethod().getReturnType().isInstance(originalValue);
    }

    @Override
    public Object adapt(Invocation invocation, Object originalValue) {
        return originalValue;
    }

}
