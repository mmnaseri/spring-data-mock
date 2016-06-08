package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.util.ClassUtils;

import java.io.Serializable;
import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/8/16, 1:43 AM)
 */
final class IdPropertyResolverUtils {

    private static final String JAVAX_PERSISTENCE_ID = "javax.persistence.Id";

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
    static String getPropertyNameFromAnnotatedMethod(Class<?> entityType, Class<? extends Serializable> idType, Method idAnnotatedMethod) {
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
    static boolean isAnnotated(AnnotatedElement element) {
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
        annotations.add(Id.class);
        final ClassLoader classLoader = ClassUtils.getDefaultClassLoader();
        if (ClassUtils.isPresent(JAVAX_PERSISTENCE_ID, classLoader)) {
            final Class<?> loadedClass;
            try {
                loadedClass = ClassUtils.forName(JAVAX_PERSISTENCE_ID, classLoader);
                annotations.add(loadedClass.asSubclass(Annotation.class));
            } catch (ClassNotFoundException ignored) {
            }
        }
        return annotations;
    }

}
