package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.error.UnknownDataOperationException;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingHandler;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class NonDataOperationInvocationHandlerTest {

    @Test
    public void testHandlingKnownMethods() throws Throwable {
        final NonDataOperationInvocationHandler handler = new NonDataOperationInvocationHandler();
        final Object proxy = new Object();
        assertThat(handler.invoke(proxy, Object.class.getMethod("hashCode"), new Object[]{}),
                   Matchers.is(proxy.hashCode()));
        assertThat(handler.invoke(proxy, Object.class.getMethod("toString"), new Object[]{}),
                   Matchers.is(proxy.toString()));
        assertThat(handler.invoke(proxy, Object.class.getMethod("equals", Object.class), new Object[]{proxy}),
                   Matchers.is(true));
        assertThat(handler.invoke(proxy, Object.class.getMethod("equals", Object.class), new Object[]{new Object()}),
                   Matchers.is(false));
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