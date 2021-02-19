package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;

import java.lang.reflect.Method;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 1:30 PM)
 */
public class SpyingHandler implements NonDataOperationHandler {

    private boolean called;

    public SpyingHandler() {
        called = false;
    }

    @Override
    public boolean handles(Object proxy, Method method, Object... args) {
        return method.getName().equals("wait");
    }

    @Override
    public Object invoke(Object proxy, Method method, Object... args) {
        called = true;
        return null;
    }

    public boolean isCalled() {
        return called;
    }

}
