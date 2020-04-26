package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.sample.models.*;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
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

    /**
     * Regression test for https://github.com/mmnaseri/spring-data-mock/issues/55
     *
     * @throws Exception
     */
    @Test
    public void testResolvingIdPropertyWhenIdAnnotationOnGetterIsFromJPA() throws Exception {
        final String property = getIdPropertyResolver().resolve(EntityWithAnnotatedIdGetterFromJPA.class,
                                                                Integer.class);
        assertThat(property, is(notNullValue()));
        assertThat(property, is("myCustomId"));
    }

    @Test
    public void testResolvingIdPropertyWhenEmbeddedIdAnnotationOnGetterIsFromJPA() throws Exception {
        final String property = getIdPropertyResolver().resolve(EntityWithAnnotatedEmbeddedIdGetterFromJPA.class, EmbeddableId.class);
        assertThat(property, is(notNullValue()));
        assertThat(property, is("myCustomId"));
    }

}