package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.Invocation;

/**
 * <p>This interface is used to encapsulate the process of adapting results from a data store operation to that
 * of the invoked repository method.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/24/15)
 */
public interface ResultAdapter<E> extends Comparable<ResultAdapter> {

    /**
     * Called to determine whether or not this adapter can adapt the original value passed down from a data
     * store operation to the type required by the repository method being invoked
     * @param invocation       the invocation
     * @param originalValue    the original value
     * @return {@literal true} if the adapter can convert the value
     */
    boolean accepts(Invocation invocation, Object originalValue);

    /**
     * Called when we need to adapt the result from an invocation to the result required by the repository method
     * @param invocation       the repository method invocation
     * @param originalValue    the original value returned from a data store operation
     * @return the adapted value
     */
    E adapt(Invocation invocation, Object originalValue);

    /**
     * @return the priority for this adapter
     */
    int getPriority();

}
