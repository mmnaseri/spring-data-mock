package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface ResultAdaptersAnd extends MappingContext {

    <E> ResultAdaptersAnd and(ResultAdapter<E> adapter);

}
