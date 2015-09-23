package com.mmnaseri.utils.spring.data.tools;

import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class AnnotationMethodFilter implements ReflectionUtils.MethodFilter {

    private final Class<? extends Annotation> annotationType;

    public AnnotationMethodFilter(Class<? extends Annotation> annotationType) {
        this.annotationType = annotationType;
    }

    @Override
    public boolean matches(Method method) {
        return AnnotationUtils.findAnnotation(method, annotationType) != null;
    }
}
