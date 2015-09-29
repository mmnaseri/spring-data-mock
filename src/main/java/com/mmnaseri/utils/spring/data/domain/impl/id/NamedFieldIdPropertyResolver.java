package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class NamedFieldIdPropertyResolver implements IdPropertyResolver {

    @Override
    public String resolve(Class<?> entityType, Class<? extends Serializable> idType) {
        final Field field = ReflectionUtils.findField(entityType, "id");
        if (field != null && idType.isAssignableFrom(field.getType())) {
            return field.getName();
        }
        return null;
    }

}
