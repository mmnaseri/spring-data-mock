package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import org.springframework.data.annotation.Id;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("unused")
public class AnnotatedFieldIdPropertyResolverTest extends BaseRepeatableIdPropertyResolverTest {

    public static class EntityWithAnnotatedIdField {

        @Id
        private String id;

    }

    public static class EntityWithMultipleAnnotatedFields {

        @Id
        private String first;

        @Id
        private String second;

    }

    public static class EntityWithoutAnnotatedFields {}

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