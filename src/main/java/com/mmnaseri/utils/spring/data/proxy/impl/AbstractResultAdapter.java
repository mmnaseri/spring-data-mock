package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public abstract class AbstractResultAdapter<E> implements ResultAdapter<E> {

    private final int priority;

    public AbstractResultAdapter(int priority) {
        this.priority = priority;
    }

    @Override
    public int compareTo(ResultAdapter that) {
        return Integer.compare(getPriority(), that.getPriority());
    }

    @Override
    public int getPriority() {
        return priority;
    }

}
