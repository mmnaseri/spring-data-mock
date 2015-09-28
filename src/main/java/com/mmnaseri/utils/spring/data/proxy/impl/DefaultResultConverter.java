package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;

import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
public class DefaultResultConverter implements ResultConverter {

    private final List<ResultConverter> converters;

    public DefaultResultConverter() {
        converters = new LinkedList<ResultConverter>();
        converters.add(new IteratorToIterableConverter());
        converters.add(new SingleValueToIterableConverter());
        converters.add(new FutureToIterableConverter());
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
