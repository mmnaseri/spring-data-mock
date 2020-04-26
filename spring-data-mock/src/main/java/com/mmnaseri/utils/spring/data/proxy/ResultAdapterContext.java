package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Collection;

/**
 * <p>This interface is used to register result adapters and later call on them to adapt the results to
 * the appropriate type.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public interface ResultAdapterContext {

    /**
     * Registers a new result adapter with this context
     *
     * @param adapter the adapter
     */
    void register(ResultAdapter<?> adapter);

    /**
     * Will call the registered result adapters to adapt the results to the required type.
     *
     * @param invocation     the invocation for which we are adapting the results
     * @param originalResult the original result from the data store operation
     * @return the adapted result
     */
    Object adapt(Invocation invocation, Object originalResult);

    /**
     * @return all the adapters registered with this context
     */
    Collection<ResultAdapter<?>> getAdapters();

}
