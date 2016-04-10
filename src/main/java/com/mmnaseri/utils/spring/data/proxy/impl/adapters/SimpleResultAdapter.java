package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

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
        if (originalValue == null) {
            return false;
        }
        final Class<?> returnType = invocation.getMethod().getReturnType();
        return !Iterable.class.isAssignableFrom(returnType) &&
                !Iterator.class.isAssignableFrom(returnType) &&
                !Future.class.isAssignableFrom(returnType);
    }

    @Override
    protected Object doAdapt(Invocation invocation, Iterable iterable) {
        final Iterator iterator = iterable.iterator();
        if (iterator.hasNext()) {
            final Object value = iterator.next();
            if (iterator.hasNext()) {
                throw new ResultAdapterFailureException(iterable, invocation.getMethod().getReturnType(), "Expected only one item but found many");
            }
            if (!PropertyUtils.getTypeOf(invocation.getMethod().getReturnType()).isInstance(value)) {
                throw new ResultAdapterFailureException(value, invocation.getMethod().getReturnType(), "Expected value to be of the indicated type");
            }
            return value;
        } else {
            return null;
        }
    }

}
