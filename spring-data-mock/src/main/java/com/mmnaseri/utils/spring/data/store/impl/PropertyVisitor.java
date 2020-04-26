package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * This property visitor will visit all properties (fields and getter methods) to find out the property that has the
 * specified annotation. The property can later be retrieved by calling {@link #getProperty()}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
class PropertyVisitor implements ReflectionUtils.MethodCallback, ReflectionUtils.FieldCallback {

    private final Class<? extends Annotation> annotationType;
    private String property;

    PropertyVisitor(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
        if (property != null) {
            return;
        }
        if (AnnotationUtils.findAnnotation(method, annotationType) != null) {
            property = PropertyUtils.getPropertyName(method);
        }
    }

    @Override
    public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
        if (property != null) {
            return;
        }
        if (field.isAnnotationPresent(annotationType)) {
            property = field.getName();
        }
    }

    public String getProperty() {
        return property;
    }

}
