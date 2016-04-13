package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;
import com.mmnaseri.utils.spring.data.proxy.impl.NonDataOperationInvocationHandler;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public interface OperationHandlers extends EventListener {

    EventListener withOperationHandlers(NonDataOperationInvocationHandler invocationHandler);

    OperationHandlersAnd withOperationHandler(NonDataOperationHandler handler);

}
