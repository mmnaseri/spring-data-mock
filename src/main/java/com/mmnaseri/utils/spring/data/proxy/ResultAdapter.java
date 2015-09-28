package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public interface ResultAdapter<E> extends Comparable<ResultAdapter> {

    boolean accepts(Invocation invocation, Object originalValue);

    E adapt(Invocation invocation, Object originalValue);

    int getPriority();

}
