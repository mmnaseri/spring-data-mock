package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface ResultAdapters extends MappingContext {

    /**
     * Tells the builder to use the provided context
     *
     * @param context the context
     * @return the rest of the configuration
     */
    MappingContext withAdapters(ResultAdapterContext context);

    /**
     * Tells the build to register a result adapter
     *
     * @param adapter the adapter
     * @param <E>     the type of the result for the adapter
     * @return the rest of the configuration
     */
    <E> ResultAdaptersAnd adaptResultsUsing(ResultAdapter<E> adapter);

}
