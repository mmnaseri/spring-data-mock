package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

/**
 * Lets us define an extra operation handler
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
@SuppressWarnings("WeakerAccess")
public interface OperationHandlersAnd extends EventListener {

    /**
     * Registers an extra operation handler
     *
     * @param handler the handler
     * @return the rest of the configuration
     */
    OperationHandlersAnd and(NonDataOperationHandler handler);

}
