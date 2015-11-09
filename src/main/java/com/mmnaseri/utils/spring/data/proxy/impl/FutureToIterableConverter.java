package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;

import java.util.concurrent.Future;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class FutureToIterableConverter extends AbstractResultConverter {

    public FutureToIterableConverter() {
    }

    @Override
    protected Object doConvert(Invocation invocation, Object original) {
        if (original instanceof Future) {
            Future future = (Future) original;
            final ResultConverter converter = new DefaultResultConverter();
            try {
                final Object result = future.get();
                return converter.convert(invocation, result);
            } catch (Exception e) {
                throw new IllegalStateException("Failed to retrieve promised result", e);
            }
        }
        return original;
    }

}
