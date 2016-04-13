package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public interface OperationHandlersAnd extends EventListener {

    OperationHandlersAnd and(NonDataOperationHandler handler);

}
