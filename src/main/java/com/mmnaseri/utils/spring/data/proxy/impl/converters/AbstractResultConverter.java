package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public abstract class AbstractResultConverter implements ResultConverter {

    @Override
    public Object convert(Invocation invocation, Object original) {
        if (original == null || invocation.getMethod().getReturnType().equals(void.class)) {
            return null;
        }
        if (invocation.getMethod().getReturnType().isInstance(original)) {
            return original;
        }
        return doConvert(invocation, original);
    }

    protected abstract Object doConvert(Invocation invocation, Object original);

}
