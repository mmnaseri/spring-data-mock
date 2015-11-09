package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public abstract class AbstractRandomKeyGenerator<S extends Serializable> implements KeyGenerator<S> {

    private final Set<S> used = new HashSet<S>();

    @Override
    public synchronized S generate() {
        S value;
        do {
            value = getNext();
        } while (used.contains(value));
        used.add(value);
        return value;
    }

    protected abstract S getNext();

}
