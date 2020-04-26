package com.mmnaseri.utils.spring.data.sample.usecases.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 11:15 AM)
 */
public class InformationExposingInvocationHandler<E> implements InvocationHandler {

    private final InformationExposingRepository implementation;
    private final E instance;

    public InformationExposingInvocationHandler(InformationExposingRepository implementation, E instance) {
        this.implementation = implementation;
        this.instance = instance;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        if (method.getDeclaringClass().equals(InformationExposingRepository.class)) {
            return method.invoke(implementation, args);
        }
        return method.invoke(instance, args);
    }

}
