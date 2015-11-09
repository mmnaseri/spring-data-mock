package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.MethodInvocationDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.DataOperationResolver;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class SignatureDataOperationResolver implements DataOperationResolver {

    private final List<TypeMapping<?>> mappings;

    public SignatureDataOperationResolver(List<TypeMapping<?>> mappings) {
        this.mappings = mappings;
    }

    @Override
    public DataStoreOperation<?, ?, ?> resolve(Method method) {
        for (TypeMapping<?> mapping : mappings) {
            final Class<?> type = mapping.getType();
            final Method declaration = findMethod(type, method.getName(), method.getParameterTypes());
            if (declaration != null) {
                final Object instance = mapping.getInstance();
                return new MethodInvocationDataStoreOperation<Serializable, Object>(instance, declaration);
            }
        }
        return null;
    }

    private static Method findMethod(Class<?> type, String name, Class<?>... parameterTypes) {
        Class<?> searchType = type;
        while (searchType != null) {
            final Method[] methods = searchType.isInterface() ? searchType.getMethods() : searchType.getDeclaredMethods();
            for (Method method : methods) {
                if (method.getName().equals(name)) {
                    if (parameterTypes == null) {
                        return method;
                    } else if (parameterTypes.length == method.getParameterTypes().length) {
                        boolean matches = true;
                        for (int i = 0; i < parameterTypes.length; i++) {
                            final Class<?> parameterType = parameterTypes[i];
                            if (!method.getParameterTypes()[i].isAssignableFrom(parameterType)) {
                                matches = false;
                                break;
                            }
                        }
                        if (matches) {
                            return method;
                        }
                    }
                }
            }
            searchType = searchType.getSuperclass();
        }
        return null;
    }

}
