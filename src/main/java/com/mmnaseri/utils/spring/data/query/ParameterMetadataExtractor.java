package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public interface ParameterMetadataExtractor<E> {

    E extract(Invocation invocation);

}
