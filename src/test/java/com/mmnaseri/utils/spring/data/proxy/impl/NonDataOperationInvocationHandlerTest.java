package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.error.UnknownDataOperationException;
import com.mmnaseri.utils.spring.data.proxy.NonDataOperationHandler;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.lang.reflect.Method;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class NonDataOperationInvocationHandlerTest {

    private static class SpyingHandler implements NonDataOperationHandler {

        private boolean called;

        private SpyingHandler() {
            called = false;
        }

        @Override
        public boolean handles(Object proxy, Method method, Object... args) {
            return method.getName().equals("wait");
        }

        @Override
        public Object invoke(Object proxy, Object... args) {
            called = true;
            return null;
        }

        public boolean isCalled() {
            return called;
        }

    }

    @Test
    public void testHandlingKnownMethods() throws Throwable {
        final NonDataOperationInvocationHandler handler = new NonDataOperationInvocationHandler();
        final Object proxy = new Object();
        assertThat(handler.invoke(proxy, Object.class.getMethod("hashCode"), new Object[]{}), Matchers.<Object>is(proxy.hashCode()));
        assertThat(handler.invoke(proxy, Object.class.getMethod("toString"), new Object[]{}), Matchers.<Object>is(proxy.toString()));
        assertThat(handler.invoke(proxy, Object.class.getMethod("equals", Object.class), new Object[]{proxy}), Matchers.<Object>is(true));
        assertThat(handler.invoke(proxy, Object.class.getMethod("equals", Object.class), new Object[]{new Object()}), Matchers.<Object>is(false));
    }

    @Test(expectedExceptions = UnknownDataOperationException.class)
    public void testUnknownMethod() throws Throwable {
        final NonDataOperationInvocationHandler handler = new NonDataOperationInvocationHandler();
        handler.invoke(new Object(), Object.class.getMethod("wait"), new Object[]{});
    }

    @Test
    public void testRegisteringMethod() throws Throwable {
        final NonDataOperationInvocationHandler handler = new NonDataOperationInvocationHandler();
        final SpyingHandler spy = new SpyingHandler();
        handler.register(spy);
        assertThat(spy.isCalled(), is(false));
        handler.invoke(new Object(), Object.class.getMethod("wait"), new Object[0]);
        assertThat(spy.isCalled(), is(true));
    }

}