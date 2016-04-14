package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.ResultConverter;

import java.util.LinkedList;
import java.util.List;

/**
 * <p>This class is the default result converter that also acts as a registry for other converters. It will
 * execute the default converters in the following order</p>
 *
 * <ol>
 *     <li>{@link FutureToIterableConverter}</li>
 *     <li>{@link IteratorToIterableConverter}</li>
 *     <li>{@link SingleValueToIterableConverter}</li>
 * </ol>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/28/15)
 */
@SuppressWarnings("WeakerAccess")
public class DefaultResultConverter implements ResultConverter {

    private final List<ResultConverter> converters;

    /**
     * Instantiates the converter and registers the default converters
     */
    public DefaultResultConverter() {
        this(true);
    }

    /**
     * Instantiates the converter
     * @param registerDefaults    whether or not default converters should be registered
     */
    public DefaultResultConverter(boolean registerDefaults) {
        converters = new LinkedList<>();
        if (registerDefaults) {
            converters.add(new FutureToIterableConverter());
            converters.add(new IteratorToIterableConverter());
            converters.add(new SingleValueToIterableConverter());
        }
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
