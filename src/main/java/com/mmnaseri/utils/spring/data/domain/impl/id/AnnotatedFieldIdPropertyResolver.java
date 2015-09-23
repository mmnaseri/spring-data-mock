package com.mmnaseri.utils.spring.data.domain.impl.id;

import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class AnnotatedFieldIdPropertyResolver extends AbstractIdPropertyResolver {

    @Override
    public String resolve(Class<?> entityType, Class<? extends Serializable> idType) {
        final AtomicReference<Field> found = new AtomicReference<Field>();
        ReflectionUtils.doWithFields(entityType, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (field.isAnnotationPresent(Id.class)) {
                    if (found.get() == null) {
                        found.set(field);
                    } else {
                        throw new IllegalStateException("More than one method found annotated with " + Id.class);
                    }
                }
            }
        });
        final Field idAnnotatedField = found.get();
        if (idAnnotatedField != null) {
            if (!idType.isAssignableFrom(idAnnotatedField.getType())) {
                throw new IllegalStateException("Expected the ID field to be of type " + idType);
            } else {
                return idAnnotatedField.getName();
            }
        }
        return null;
    }

}
