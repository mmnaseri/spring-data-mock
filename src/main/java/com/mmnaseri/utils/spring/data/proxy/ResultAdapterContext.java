package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Collection;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/24/15)
 */
public interface ResultAdapterContext {

    void register(ResultAdapter<?> adapter);

    Object adapt(Invocation invocation, Object originalResult);

    Collection<ResultAdapter<?>> getAdapters();

}
