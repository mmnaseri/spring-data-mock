package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/19/15)
 */
public interface ParameterMetadataExtractor<E> {

    E extract(Invocation invocation);

}
