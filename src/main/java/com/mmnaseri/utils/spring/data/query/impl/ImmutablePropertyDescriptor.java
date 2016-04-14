package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.query.PropertyDescriptor;

/**
 * This is an immutable property descriptor
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/20/15)
 */
public class ImmutablePropertyDescriptor implements PropertyDescriptor {

    private final String path;
    private final Class<?> type;

    public ImmutablePropertyDescriptor(String path, Class<?> type) {
        this.path = path;
        this.type = type;
    }

    @Override
    public String getPath() {
        return path;
    }

    @Override
    public Class<?> getType() {
        return type;
    }

}
