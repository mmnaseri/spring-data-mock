package com.agileapes.utils.spring;

import java.lang.reflect.Type;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:14)
 */
public interface MethodDescriptor {

    Class<?> getDeclaringClass();

    String getName();

    Type getReturnType();

    Type[] getParameterTypes();

}
