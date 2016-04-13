package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;
import com.mmnaseri.utils.spring.data.proxy.impl.converters.FutureToIterableConverter;
import com.mmnaseri.utils.spring.data.proxy.impl.converters.IteratorToIterableConverter;
import com.mmnaseri.utils.spring.data.proxy.impl.converters.SingleValueToIterableConverter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
public class DefaultResultConverter implements ResultConverter {

    private final List<ResultConverter> converters;

    public DefaultResultConverter() {
        converters = new LinkedList<>();
        converters.add(new FutureToIterableConverter());
        converters.add(new IteratorToIterableConverter());
        converters.add(new SingleValueToIterableConverter());
    }

    @Override
    public Object convert(Invocation invocation, Object original) {
        Object value = original;
        for (ResultConverter converter : converters) {
            value = converter.convert(invocation, value);
        }
        return value;
    }

}
