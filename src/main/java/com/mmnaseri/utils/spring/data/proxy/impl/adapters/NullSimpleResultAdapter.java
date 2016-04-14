package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;

import java.util.Iterator;
import java.util.concurrent.Future;

/**
 * <p>This adapter will try to adapt a {@literal null} value to a simple value. Simple here is defined as anything
 * that is not an iterable, an iterator, or a future promise.</p>
 *
 * <p>It adapts results if the return type is simple and the original value is {@literal null}.</p>
 *
 * <p>This adapter runs at the priority of {@literal -400}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/24/15)
 */
public class NullSimpleResultAdapter extends AbstractResultAdapter<Object> {

    public NullSimpleResultAdapter() {
        super(-400);
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        final Class<?> returnType = invocation.getMethod().getReturnType();
        return !Iterable.class.isAssignableFrom(returnType) &&
                !Iterator.class.isAssignableFrom(returnType) &&
                !Future.class.isAssignableFrom(returnType) &&
                originalValue == null;
    }

    @Override
    public Object adapt(Invocation invocation, Object originalValue) {
        if (invocation.getMethod().getReturnType().isPrimitive()) {
            throw new ResultAdapterFailureException(null, invocation.getMethod().getReturnType());
        }
        return null;
    }

}
