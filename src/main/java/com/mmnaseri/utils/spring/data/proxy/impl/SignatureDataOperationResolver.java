package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.MethodInvocationDataStoreOperation;
import com.mmnaseri.utils.spring.data.proxy.DataOperationResolver;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.store.DataStoreOperation;
import org.springframework.util.ReflectionUtils;

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
            final Method declaration = ReflectionUtils.findMethod(type, method.getName(), method.getParameterTypes());
            if (declaration != null) {
                final Object instance = mapping.getInstance();
                return new MethodInvocationDataStoreOperation<Serializable, Object>(instance, declaration);
            }
        }
        return null;
    }

}
