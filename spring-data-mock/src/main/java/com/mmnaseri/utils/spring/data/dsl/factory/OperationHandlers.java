package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;

/**
 * Lets us define the operation handlers
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
@SuppressWarnings("WeakerAccess")
public interface OperationHandlers extends FallbackKeyGenerator {

    /**
     * Sets the invocation handler used for handling non-data-related operations
     *
     * @param invocationHandler the invocation handler
     * @return the rest of the configuration
     */
    FallbackKeyGenerator withOperationHandlers(NonDataOperationInvocationHandler invocationHandler);

    /**
     * Registers an operation handler
     *
     * @param handler the handler
     * @return the rest of the configuration
     */
    OperationHandlersAnd withOperationHandler(NonDataOperationHandler handler);

}
