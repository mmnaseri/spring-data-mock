package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.tools.GetterMethodFilter;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.concurrent.atomic.AtomicReference;

/**
 * This class is for resolving an ID based on the getter. It will try to find a getter for a property named
 * {@literal "id"} -- i.e., it will look for a getter named "getId".
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/23/15)
 */
@SuppressWarnings("WeakerAccess")
public class NamedGetterIdPropertyResolver extends AnnotatedIdPropertyResolver {

    @Override
    public String resolve(Class<?> entityType, final Class<? extends Serializable> idType) {
        final AtomicReference<Method> found = new AtomicReference<>();
        ReflectionUtils.doWithMethods(entityType, new ReflectionUtils.MethodCallback() {
            @Override
            public void doWith(Method method) throws IllegalArgumentException, IllegalAccessException {
                if (PropertyUtils.getPropertyName(method).equals("id")) {
                    found.set(method);
                }
            }
        }, new GetterMethodFilter());
        final Method idAnnotatedMethod = found.get();
        return getPropertyNameFromAnnotatedMethod(entityType, idType, idAnnotatedMethod);
    }

}
