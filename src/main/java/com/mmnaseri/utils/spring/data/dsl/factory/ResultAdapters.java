package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapterContext;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface ResultAdapters extends MappingContext {

    MappingContext withAdapters(ResultAdapterContext context);

    <E> ResultAdaptersAnd adaptResultsUsing(ResultAdapter<E> adapter);

}
