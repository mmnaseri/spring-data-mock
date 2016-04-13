package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import java.util.Iterator;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class EmptyIterator implements Iterator {

    @Override
    public boolean hasNext() {
        return false;
    }

    @Override
    public Object next() {
        throw new IndexOutOfBoundsException();
    }

    @Override
    public void remove() {
        throw new IndexOutOfBoundsException();
    }

}
