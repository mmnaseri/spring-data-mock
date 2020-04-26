package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.error.UnknownDataOperationException;
import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;
import com.mmnaseri.utils.spring.data.proxy.impl.regular.EqualsNonDataOperationHandler;
import com.mmnaseri.utils.spring.data.proxy.impl.regular.HashCodeNonDataOperationHandler;
import com.mmnaseri.utils.spring.data.proxy.impl.regular.ToStringNonDataOperationHandler;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

/**
 * This class will handle invocations that are strictly non-data-related.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class NonDataOperationInvocationHandler implements InvocationHandler {

    private static final Log log = LogFactory.getLog(NonDataOperationInvocationHandler.class);
    private final List<NonDataOperationHandler> handlers;

    public NonDataOperationInvocationHandler() {
        this(true);
    }

    public NonDataOperationInvocationHandler(boolean registerDefaults) {
        handlers = new LinkedList<>();
        if (registerDefaults) {
            log.info("Registering all the default operation handlers");
            handlers.add(new EqualsNonDataOperationHandler());
            handlers.add(new HashCodeNonDataOperationHandler());
            handlers.add(new ToStringNonDataOperationHandler());
        }
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        log.info("Intercepting non-data method " + method);
        for (NonDataOperationHandler handler : handlers) {
            if (handler.handles(proxy, method, args)) {
                log.info("Found handler " + handler + " for method " + method);
                return handler.invoke(proxy, args);
            }
        }
        log.error("No data or non-data operation handler could be found for method " + method);
        throw new UnknownDataOperationException(method);
    }

    public void register(NonDataOperationHandler handler) {
        log.info("Registering operation handler " + handler);
        handlers.add(handler);
    }

    public List<NonDataOperationHandler> getHandlers() {
        return Collections.unmodifiableList(handlers);
    }

}
