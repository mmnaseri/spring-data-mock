package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithAnnotatedIdField;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithMultipleAnnotatedFields;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithoutAnnotatedFields;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("unused")
public class AnnotatedFieldIdPropertyResolverTest extends BaseRepeatableIdPropertyResolverTest {

    @Override
    protected IdPropertyResolver getIdPropertyResolver() {
        return new AnnotatedFieldIdPropertyResolver();
    }

    @Override
    protected Class<?> properEntity() {
        return EntityWithAnnotatedIdField.class;
    }

    @Override
    protected Class<?> entityWithNoProperty() {
        return EntityWithoutAnnotatedFields.class;
    }

    @Override
    protected Class<?> entityWithMultipleProperties() {
        return EntityWithMultipleAnnotatedFields.class;
    }

}