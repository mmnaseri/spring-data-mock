package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public interface OperationHandlersAnd extends EventListener {

    OperationHandlersAnd and(NonDataOperationHandler handler);

}
