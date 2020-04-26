package com.mmnaseri.utils.spring.data.query;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * This interface encapsulates the process of extracting metadata for a query execution from a given query method
 * invocation.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
@SuppressWarnings("WeakerAccess")
public interface ParameterMetadataExtractor<E> {

    /**
     * Extracts the promised metadata as per this invocation.
     *
     * @param invocation the invocation
     * @return the promised metadata or {@literal null} if nothing relevant can be found
     */
    E extract(Invocation invocation);

}
