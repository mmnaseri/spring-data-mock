package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.NoIdPropertyException;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public class EntityIdPropertyResolver implements IdPropertyResolver {

    private final AnnotatedGetterIdPropertyResolver annotatedGetterIdPropertyResolver = new AnnotatedGetterIdPropertyResolver();
    private final AnnotatedFieldIdPropertyResolver annotatedFieldIdPropertyResolver = new AnnotatedFieldIdPropertyResolver();
    private final NamedGetterIdPropertyResolver namedGetterIdPropertyResolver = new NamedGetterIdPropertyResolver();
    private final NamedFieldIdPropertyResolver namedFieldIdPropertyResolver = new NamedFieldIdPropertyResolver();

    @Override
    public String resolve(Class<?> entityType, Class<? extends Serializable> idType) {
        String idProperty = annotatedGetterIdPropertyResolver.resolve(entityType, idType);
        if (idProperty == null) {
            idProperty = annotatedFieldIdPropertyResolver.resolve(entityType, idType);
        }
        if (idProperty == null) {
            idProperty = namedGetterIdPropertyResolver.resolve(entityType, idType);
        }
        if (idProperty == null) {
            idProperty = namedFieldIdPropertyResolver.resolve(entityType, idType);
        }
        if (idProperty == null) {
            throw new NoIdPropertyException(entityType);
        }
        return idProperty;
    }

}
