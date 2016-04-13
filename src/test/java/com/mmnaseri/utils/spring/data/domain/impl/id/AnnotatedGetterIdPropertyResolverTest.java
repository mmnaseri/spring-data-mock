package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithAnnotatedIdGetter;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithMultipleAnnotatedIdGetters;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithoutAnnotatedIdGetter;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("unused")
public class AnnotatedGetterIdPropertyResolverTest extends BaseRepeatableIdPropertyResolverTest {

    @Override
    protected IdPropertyResolver getIdPropertyResolver() {
        return new AnnotatedGetterIdPropertyResolver();
    }

    @Override
    protected Class<?> properEntity() {
        return EntityWithAnnotatedIdGetter.class;
    }

    @Override
    protected Class<?> entityWithNoProperty() {
        return EntityWithoutAnnotatedIdGetter.class;
    }

    @Override
    protected Class<?> entityWithMultipleProperties() {
        return EntityWithMultipleAnnotatedIdGetters.class;
    }

}