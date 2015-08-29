/*
 * Copyright (c) 2014 Milad Naseri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agileapes.utils.spring.impl;

import com.agileapes.utils.spring.InterceptionStrategy;
import com.agileapes.utils.spring.InterfaceInterceptor;
import com.agileapes.utils.spring.MethodDescriptor;
import com.agileapes.utils.spring.RepositoryDescriptor;
import com.agileapes.utils.spring.impl.interceptors.DefaultDataInterfaceInterceptor;
import com.agileapes.utils.spring.impl.interceptors.JpaDataInterfaceInterceptor;
import com.agileapes.utils.spring.impl.interceptors.MongoDataInterfaceInterceptor;
import com.agileapes.utils.spring.impl.strategies.CountQueryMethodStrategy;
import com.agileapes.utils.spring.impl.strategies.SimpleLookupQueryMethodStrategy;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.data.repository.Repository;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/22 AD, 18:46)
 */
public class RepositoryMethodInterceptor<E, K extends Serializable, R extends Repository<E, K>> implements MethodInterceptor {

    private static final boolean dataCommonsPresent = ClassUtils.isPresent("org.springframework.data.repository.PagingAndSortingRepository", RepositoryMethodInterceptor.class.getClassLoader());
    private static final boolean dataJpaPresent = ClassUtils.isPresent("org.springframework.data.jpa.repository.JpaRepository", RepositoryMethodInterceptor.class.getClassLoader());
    private static final boolean dataMongoPresent = ClassUtils.isPresent("org.springframework.data.mongodb.repository.MongoRepository", RepositoryMethodInterceptor.class.getClassLoader());
    private final RepositoryDescriptor<E, K, R> descriptor;
    private final List<InterfaceInterceptor> interceptors;
    private final Map<K, E> data;
    private final List<InterceptionStrategy> strategies;

    private static Method findOriginalDeclaration(Method method) {
        return findOriginalDeclaration(method, method.getDeclaringClass());
    }

    private static Method findOriginalDeclaration(Method method, Class<?> search) {
        if (search.getSuperclass() != null) {
            final Class<?> superclass = search.getSuperclass();
            try {
                return findOriginalDeclaration(superclass.getMethod(method.getName(), method.getParameterTypes()), superclass);
            } catch (NoSuchMethodException ignored) {}
        }
        if (search.getInterfaces() != null) {
            for (Class<?> superclass : search.getInterfaces()) {
                try {
                    return findOriginalDeclaration(superclass.getMethod(method.getName(), method.getParameterTypes()), superclass);
                } catch (NoSuchMethodException ignored) {}
            }
        }
        return method;
    }

    public RepositoryMethodInterceptor(RepositoryDescriptor<E, K, R> descriptor) {
        this.descriptor = descriptor;
        this.interceptors = new ArrayList<InterfaceInterceptor>();
        this.data = new HashMap<K, E>(descriptor.getData());
        this.strategies = new ArrayList<InterceptionStrategy>();
        if (dataCommonsPresent) {
            interceptors.add(new DefaultDataInterfaceInterceptor<E, K>(data, descriptor));
            if (dataJpaPresent) {
                interceptors.add(new JpaDataInterfaceInterceptor<E, K>(data, descriptor));
            }
            if (dataMongoPresent) {
                interceptors.add(new MongoDataInterfaceInterceptor<E, K>(data, descriptor));
            }
            strategies.add(new SimpleLookupQueryMethodStrategy<E, K, R>(data, descriptor));
            strategies.add(new CountQueryMethodStrategy<E, K, R>(data, descriptor));
        }
    }

    @Override
    public Object intercept(Object target, Method method, Object[] parameters, MethodProxy proxy) throws Throwable {
        final Method declaration = findOriginalDeclaration(method);
        for (MethodDescriptor methodDescriptor : descriptor.getOperations().keySet()) {
            if (declaration.getName().equals(methodDescriptor.getName()) && Arrays.equals(declaration.getGenericParameterTypes(), methodDescriptor.getParameterTypes()) && declaration.getGenericReturnType().equals(methodDescriptor.getReturnType()) && declaration.getDeclaringClass().isAssignableFrom(methodDescriptor.getDeclaringClass())) {
                return descriptor.getOperations().get(methodDescriptor).execute(data, parameters);
            }
        }
        for (InterfaceInterceptor interceptor : interceptors) {
            if (declaration.getDeclaringClass().isInstance(interceptor)) {
                return declaration.invoke(interceptor, parameters);
            }
        }
        for (InterceptionStrategy strategy : strategies) {
            if (strategy.intercepts(method)) {
                return strategy.call(target, method, parameters);
            }
        }
        throw new UnsupportedOperationException(method.toGenericString());
    }

}
