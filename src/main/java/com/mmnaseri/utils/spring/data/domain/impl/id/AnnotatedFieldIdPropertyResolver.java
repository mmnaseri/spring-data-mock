package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.MultipleIdPropertiesException;
import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class AnnotatedFieldIdPropertyResolver implements IdPropertyResolver {

    @Override
    public String resolve(final Class<?> entityType, Class<? extends Serializable> idType) {
        final AtomicReference<Field> found = new AtomicReference<Field>();
        ReflectionUtils.doWithFields(entityType, new ReflectionUtils.FieldCallback() {
            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (field.isAnnotationPresent(Id.class)) {
                    if (found.get() == null) {
                        found.set(field);
                    } else {
                        throw new MultipleIdPropertiesException(entityType, Id.class);
                    }
                }
            }
        });
        final Field idAnnotatedField = found.get();
        if (idAnnotatedField != null) {
            if (!idType.isAssignableFrom(idAnnotatedField.getType())) {
                throw new PropertyTypeMismatchException(entityType, idAnnotatedField.getName(), idType, idAnnotatedField.getType());
            } else {
                return idAnnotatedField.getName();
            }
        }
        return null;
    }

}
