package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.error.MultipleIdPropertiesException;
import com.mmnaseri.utils.spring.data.tools.GetterMethodFilter;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class will resolve ID property name from a getter method that is annotated with
 * {@link Id @Id}.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/23/15)
 */
@SuppressWarnings("WeakerAccess")
public class AnnotatedGetterIdPropertyResolver extends AnnotatedIdPropertyResolver {

    @Override
    public String resolve(final Class<?> entityType, Class<? extends Serializable> idType) {
        final AtomicReference<Method> found = new AtomicReference<>();
        ReflectionUtils.doWithMethods(entityType, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                if (AnnotationUtils.findAnnotation(method, Id.class) != null) {
                    if (found.get() == null) {
                        found.set(method);
                    } else {
                        throw new MultipleIdPropertiesException(entityType, Id.class);
                    }
                }
            }
        }, new GetterMethodFilter());
        final Method idAnnotatedMethod = found.get();
        return getPropertyNameFromAnnotatedMethod(entityType, idType, idAnnotatedMethod);
    }

}
