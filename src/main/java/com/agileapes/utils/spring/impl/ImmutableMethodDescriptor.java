package com.agileapes.utils.spring.impl;

import com.agileapes.utils.spring.MethodDescriptor;

import java.lang.reflect.Type;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:23)
 */
public class ImmutableMethodDescriptor implements MethodDescriptor {

    private final Class<?> declaringClass;
    private final String name;
    private final Type returnType;
    private final Type[] parameterTypes;

    public ImmutableMethodDescriptor(Class<?> declaringClass, String name, Type returnType, Type[] parameterTypes) {
        this.declaringClass = declaringClass;
        this.name = name;
        this.returnType = returnType;
        this.parameterTypes = parameterTypes;
    }

    @Override
    public Class<?> getDeclaringClass() {
        return declaringClass;
    }

    @Override
    public String getName() {
        return name;
    }

    @Override
    public Type getReturnType() {
        return returnType;
    }

    @Override
    public Type[] getParameterTypes() {
        return parameterTypes;
    }
}
