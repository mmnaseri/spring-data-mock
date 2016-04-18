package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.proxy.TypeMapping;

/**
 * This is an immutable type mapping.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public class ImmutableTypeMapping<E> implements TypeMapping<E> {

    private final Class<E> type;
    private final E instance;

    public ImmutableTypeMapping(Class<E> type, E instance) {
        this.type = type;
        this.instance = instance;
    }

    @Override
    public Class<E> getType() {
        return type;
    }

    @Override
    public E getInstance() {
        return instance;
    }

}
