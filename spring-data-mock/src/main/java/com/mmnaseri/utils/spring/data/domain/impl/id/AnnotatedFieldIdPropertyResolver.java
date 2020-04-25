package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.MultipleIdPropertiesException;
import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.springframework.data.annotation.Id;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.concurrent.atomic.AtomicReference;

import static com.mmnaseri.utils.spring.data.domain.impl.id.IdPropertyResolverUtils.isAnnotated;

/**
 * This class will help resolve ID property name if the entity has a field that is annotated with {@link Id @Id}
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
@SuppressWarnings("WeakerAccess")
public class AnnotatedFieldIdPropertyResolver implements IdPropertyResolver {

    @Override
    public String resolve(final Class<?> entityType, Class<?> idType) {
        final AtomicReference<Field> found = new AtomicReference<>();
        //try to find the ID field
        ReflectionUtils.doWithFields(entityType, new ReflectionUtils.FieldCallback() {

            @Override
            public void doWith(Field field) throws IllegalArgumentException, IllegalAccessException {
                if (isAnnotated(field)) {
                    if (found.get() == null) {
                        found.set(field);
                    } else {
                        throw new MultipleIdPropertiesException(entityType);
                    }
                }
            }
        });
        final Field idAnnotatedField = found.get();
        //if a field was found, try to get the ID property name
        if (idAnnotatedField != null) {
            if (!PropertyUtils.getTypeOf(idType).isAssignableFrom(
                    PropertyUtils.getTypeOf(idAnnotatedField.getType()))) {
                throw new PropertyTypeMismatchException(entityType, idAnnotatedField.getName(), idType,
                                                        idAnnotatedField.getType());
            } else {
                return idAnnotatedField.getName();
            }
        }
        return null;
    }

}
