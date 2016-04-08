package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
abstract class AnnotatedIdPropertyResolver implements IdPropertyResolver {

    protected String getPropertyNameFromAnnotatedMethod(Class<?> entityType, Class<? extends Serializable> idType, Method idAnnotatedMethod) {
        if (idAnnotatedMethod != null) {
            final String name = PropertyUtils.getPropertyName(idAnnotatedMethod);
            if (!idType.isAssignableFrom(idAnnotatedMethod.getReturnType())) {
                throw new PropertyTypeMismatchException(entityType, name, idType, idAnnotatedMethod.getReturnType());
            } else {
                return name;
            }
        }
        return null;
    }
}
