package com.mmnaseri.utils.spring.data.proxy.impl.regular;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

import java.lang.reflect.Method;

/**
 * This class will handle the {@link Object#hashCode()} method.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class HashCodeNonDataOperationHandler implements NonDataOperationHandler {

    private static final String HASH_CODE = "hashCode";

    @Override
    public boolean handles(Object proxy, Method method, Object... args) {
        return Object.class.equals(method.getDeclaringClass()) && HASH_CODE.equals(method.getName());
    }

    @Override
    public Object invoke(Object proxy, Object... args) {
        return System.identityHashCode(proxy);
    }

}
