package com.mmnaseri.utils.spring.data.proxy;

import java.lang.reflect.Method;

/**
 * This interface encapsulates how a non-data operation should be handled.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
@SuppressWarnings("unused")
public interface NonDataOperationHandler {

    /**
     * Used to find out if the given invocation on the indicated proxy object can be handled by this handler.
     *
     * @param proxy  the proxy
     * @param method the method
     * @param args   the arguments passed to the method
     * @return {@literal true} if this handler can handle the given invocation.
     */
    boolean handles(Object proxy, Method method, Object... args);

    /**
     * Used to handle the invocation. It is assumed that prior to calling this method, the caller has already verified
     * (using {@link #handles(Object, Method, Object...)}) that the handler can indeed handle this method invocation.
     *
     * @param proxy the proxy object
     * @param args  the arguments
     * @return the result
     */
    Object invoke(Object proxy, Object... args);

}
