package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * This class and its children will help resolve the ID property from an annotated element
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
@SuppressWarnings("WeakerAccess")
abstract class AnnotatedIdPropertyResolver implements IdPropertyResolver {

    /**
     * Returns the name of the property as represented by the method given
     * @param entityType           the type of the entity that the ID is being resolved for
     * @param idType               the type of the ID expected for the entity
     * @param idAnnotatedMethod    the method that will return the ID (e.g. getter for the ID property)
     * @return the name of the property, or {@literal null} if the method is {@literal null}
     */
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
