package com.mmnaseri.utils.spring.data.store.impl;

import com.mmnaseri.utils.spring.data.tools.StringUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public class PropertyVisitor implements ReflectionUtils.MethodCallback, ReflectionUtils.FieldCallback {

    private final Class<? extends Annotation> annotationType;
    private String property;

    public PropertyVisitor(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
        if (property != null) {
            return;
        }
        if (AnnotationUtils.findAnnotation(method, annotationType) != null) {
            property = StringUtils.uncapitalize(method.getName().substring(3));
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
