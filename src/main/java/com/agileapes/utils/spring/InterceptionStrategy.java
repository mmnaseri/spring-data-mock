package com.agileapes.utils.spring;

import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/23 AD, 16:53)
 */
public interface InterceptionStrategy {

    boolean intercepts(Method method);

    Object call(Object target, Method method, Object... parameters) throws Throwable;

}
