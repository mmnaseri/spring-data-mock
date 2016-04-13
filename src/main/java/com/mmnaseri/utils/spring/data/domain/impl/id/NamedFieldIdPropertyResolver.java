package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * This class is for finding a field with the name {@literal "id"}.
 *
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
@SuppressWarnings("WeakerAccess")
public class NamedFieldIdPropertyResolver implements IdPropertyResolver {

    @Override
    public String resolve(Class<?> entityType, Class<? extends Serializable> idType) {
        final Field field = ReflectionUtils.findField(entityType, "id");
        if (field != null) {
            if (idType.isAssignableFrom(field.getType())) {
                return field.getName();
            } else {
                throw new PropertyTypeMismatchException(entityType, field.getName(), idType, field.getType());
            }
        }
        return null;
    }

}
