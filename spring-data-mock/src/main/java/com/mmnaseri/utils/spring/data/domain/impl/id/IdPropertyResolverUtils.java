package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * Utilities for figuring out the specifics of the ID property
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/8/16, 1:43 AM)
 */
@SuppressWarnings("WeakerAccess")
public final class IdPropertyResolverUtils {

    private static final List<String> ID_ANNOTATIONS = new ArrayList<>();
    private static final Log log = LogFactory.getLog(IdPropertyResolverUtils.class);

    static {
        ID_ANNOTATIONS.add("org.springframework.data.annotation.Id");
        ID_ANNOTATIONS.add("javax.persistence.Id");
    }

    private IdPropertyResolverUtils() {
        throw new UnsupportedOperationException();
    }

    /**
     * Returns the name of the property as represented by the method given
     * @param entityType           the type of the entity that the ID is being resolved for
     * @param idType               the type of the ID expected for the entity
     * @param idAnnotatedMethod    the method that will return the ID (e.g. getter for the ID property)
     * @return the name of the property, or {@literal null} if the method is {@literal null}
     */
    public static String getPropertyNameFromAnnotatedMethod(Class<?> entityType, Class<? extends Serializable> idType, Method idAnnotatedMethod) {
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

    /**
     * Determines whether or not the given element is annotated with the annotations specified by
     * {@link #getIdAnnotations()}
     * @param element    the element to be examined
     * @return {@literal true} if the element has any of the ID annotations
     */
    public static boolean isAnnotated(AnnotatedElement element) {
        final List<Class<? extends Annotation>> annotations = getIdAnnotations();
        for (Class<? extends Annotation> annotation : annotations) {
            if (AnnotationUtils.findAnnotation(element, annotation) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Lists all the annotations that can be used to mark a property as the ID property
     * based on the libraries that can be found in the classpath
     * @return the list of annotations
     */
    private static List<Class<? extends Annotation>> getIdAnnotations() {
        final List<Class<? extends Annotation>> annotations = new ArrayList<>();
        final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        for (String idAnnotation : ID_ANNOTATIONS) {
            try {
                final Class<?> type = ClassUtils.forName(idAnnotation, classLoader);
                final Class<? extends Annotation> annotationType = type.asSubclass(Annotation.class);
                annotations.add(annotationType);
            } catch (ClassNotFoundException ignored) {
                //if the class for the annotation wasn't found, we just ignore it
                log.debug("Requested ID annotation type " + idAnnotation + " is not present in the classpath");
            }
        }
        return annotations;
    }

}
