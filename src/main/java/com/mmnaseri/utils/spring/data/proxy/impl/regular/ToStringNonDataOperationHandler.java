package com.mmnaseri.utils.spring.data.proxy.impl.regular;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

import java.lang.reflect.Method;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public class ToStringNonDataOperationHandler implements NonDataOperationHandler {

    private static final String TO_STRING = "toString";

    @Override
    public boolean handles(Object proxy, Method method, Object... args) {
        return Object.class.equals(method.getDeclaringClass()) && TO_STRING.equals(method.getName());
    }

    @Override
    public Object invoke(Object proxy, Object... args) {
        return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy));
    }

}
