package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.NoIdPropertyException;
import com.mmnaseri.utils.spring.data.sample.models.*;
import org.testng.annotations.Test;

import java.io.Serializable;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class EntityIdPropertyResolverTest {

    @Test
    public void testThatAnnotatedGetterHasPrecedence() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        final String resolved = resolver.resolve(EntityWithAnnotationOnIdFieldAndGetterAndAnIdField.class, Serializable.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("unannotatedId"));
    }

    @Test
    public void testThatAnnotatedPropertyIsSecond() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        final String resolved = resolver.resolve(EntityWithIdFieldAndAnAnnotatedIdField.class, Serializable.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("annotatedId"));
    }

    @Test
    public void testThatNamedGetterIsThird() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        final String resolved = resolver.resolve(EntityWithUnderscorePrecedingIdField.class, Serializable.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test
    public void testThatNamedFieldIsFourth() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        final String resolved = resolver.resolve(EntityWithIdFieldHiddenBehindDifferentlyNamedAccessors.class, Serializable.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test(expectedExceptions = NoIdPropertyException.class)
    public void testThatNoOtherValueIsHonored() throws Exception {
        final IdPropertyResolver resolver = new EntityIdPropertyResolver();
        resolver.resolve(EntityWithNoImmediatelyResolvableIdProperty.class, Serializable.class);
    }

}