package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.tools.GetterMethodFilter;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class NamedGetterIdPropertyResolver extends AbstractIdPropertyResolver {

    @Override
    public String resolve(Class<?> entityType, final Class<? extends Serializable> idType) {
        final AtomicReference<Method> found = new AtomicReference<Method>();
        ReflectionUtils.doWithMethods(entityType, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                if (uncapitalize(method.getName()).equals("id") && idType.isAssignableFrom(method.getReturnType())) {
                    found.set(method);
                }
            }
        }, new GetterMethodFilter());
        if (found.get() != null) {
            return uncapitalize(found.get().getName());
        }
        return null;
    }

}