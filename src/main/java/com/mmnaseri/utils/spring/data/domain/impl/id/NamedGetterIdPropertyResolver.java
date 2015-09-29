package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.tools.GetterMethodFilter;
import com.mmnaseri.utils.spring.data.tools.StringUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class NamedGetterIdPropertyResolver implements IdPropertyResolver {

    @Override
    public String resolve(Class<?> entityType, final Class<? extends Serializable> idType) {
        final AtomicReference<Method> found = new AtomicReference<Method>();
        ReflectionUtils.doWithMethods(entityType, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                if (StringUtils.uncapitalize(method.getName().substring(3)).equals("id")) {
                    found.set(method);
                }
            }
        }, new GetterMethodFilter());
        final Method idAnnotatedMethod = found.get();
        if (idAnnotatedMethod != null) {
            if (!idType.isAssignableFrom(idAnnotatedMethod.getReturnType())) {
                throw new IllegalStateException("Expected the ID field getter method to be of type " + idType);
            } else {
                return StringUtils.uncapitalize(idAnnotatedMethod.getName().substring(3));
            }
        }
        return null;
    }

}
