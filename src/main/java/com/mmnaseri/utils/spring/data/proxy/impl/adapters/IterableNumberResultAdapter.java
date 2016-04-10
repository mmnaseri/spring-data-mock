package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.util.Iterator;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public class IterableNumberResultAdapter extends AbstractIterableResultAdapter<Object> {

    public IterableNumberResultAdapter() {
        super(-425);
    }

    @Override
    protected Object doAdapt(Invocation invocation, Iterable iterable) {
        final Iterator iterator = iterable.iterator();
        final Object value = iterator.next();
        final Number number = (Number) value;
        final Class<?> returnType = PropertyUtils.getTypeOf(invocation.getMethod().getReturnType());
        if (Long.class.equals(returnType)) {
            return number.longValue();
        } else if (Short.class.equals(returnType)) {
            return number.shortValue();
        } else if (Integer.class.equals(returnType)) {
            return number.intValue();
        } else if (Byte.class.equals(returnType)) {
            return number.byteValue();
        } else if (Double.class.equals(returnType)) {
            return number.doubleValue();
        } else if (Float.class.equals(returnType)) {
            return number.floatValue();
        }
        throw new ResultAdapterFailureException(value, returnType);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        if (originalValue == null) {
            return false;
        }
        if (!Number.class.isAssignableFrom(PropertyUtils.getTypeOf(invocation.getMethod().getReturnType()))) {
            return false;
        }
        if (originalValue instanceof Iterable) {
            Iterable iterable = (Iterable) originalValue;
            final Iterator iterator = iterable.iterator();
            if (iterator.hasNext()) {
                final Object value = iterator.next();
                return value instanceof Number && !iterator.hasNext();
            }
        }
        return false;
    }

}
