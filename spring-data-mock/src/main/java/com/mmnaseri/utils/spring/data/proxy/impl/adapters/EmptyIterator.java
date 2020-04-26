package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import java.util.Iterator;

/**
 * This class is an iterator over an empty collection.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class EmptyIterator implements Iterator {

    /**
     * This is the shared instance of the empty iterator
     */
    public static final EmptyIterator INSTANCE = new EmptyIterator();

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
