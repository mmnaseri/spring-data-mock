package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("unused")
public class NamedFieldIdPropertyResolverTest extends BaseIdPropertyResolverTest {

    @Override
    protected IdPropertyResolver getIdPropertyResolver() {
        return new NamedFieldIdPropertyResolver();
    }

    @Override
    protected Class<?> properEntity() {
        return EntityWithIdField.class;
    }

    @Override
    protected Class<?> entityWithNoProperty() {
        return EntityWithoutIdField.class;
    }

    public static class EntityWithIdField {

        private String id;

    }


    public static class EntityWithoutIdField {

        private String name;

    }

}