package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;

/**
 * This class will let us convert non-{@literal null} values if the invocation is not of type {@literal void}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @see #doConvert(Invocation, Object)
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

    /**
     * Called to invoke a conversion of the given value to the desired result value.
     *
     * @param invocation the invocation
     * @param original   the original value
     * @return the converted value
     */
    protected abstract Object doConvert(Invocation invocation, Object original);

}
