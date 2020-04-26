package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.MultipleIdPropertiesException;
import com.mmnaseri.utils.spring.data.tools.GetterMethodFilter;
import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

import static com.mmnaseri.utils.spring.data.domain.impl.id.IdPropertyResolverUtils.getPropertyNameFromAnnotatedMethod;
import static com.mmnaseri.utils.spring.data.domain.impl.id.IdPropertyResolverUtils.isAnnotated;

/**
 * This class will resolve ID property name from a getter method that is annotated with {@link Id @Id}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
@SuppressWarnings("WeakerAccess")
public class AnnotatedGetterIdPropertyResolver implements IdPropertyResolver {

    @Override
    public String resolve(final Class<?> entityType, Class<?> idType) {
        final AtomicReference<Method> found = new AtomicReference<>();
        ReflectionUtils.doWithMethods(entityType, method -> {
            if (isAnnotated(method)) {
                if (found.get() == null) {
                    found.set(method);
                } else {
                    throw new MultipleIdPropertiesException(entityType);
                }
            }
        }, new GetterMethodFilter());
        final Method idAnnotatedMethod = found.get();
        return getPropertyNameFromAnnotatedMethod(entityType, idType, idAnnotatedMethod);
    }

}
