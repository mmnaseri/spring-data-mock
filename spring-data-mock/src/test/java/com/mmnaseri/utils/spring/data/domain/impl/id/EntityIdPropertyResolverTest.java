package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.error.NoIdPropertyException;
import com.mmnaseri.utils.spring.data.error.PrimitiveIdTypeException;
import com.mmnaseri.utils.spring.data.sample.models.*;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class EntityIdPropertyResolverTest {

    private EntityIdPropertyResolver resolver;

    @BeforeMethod
    public void setUp() {
        resolver = new EntityIdPropertyResolver();
    }

    @Test
    public void testThatAnnotatedGetterHasPrecedence() {
        final String resolved = resolver.resolve(EntityWithAnnotationOnIdFieldAndGetterAndAnIdField.class,
                                                 Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("unannotatedId"));
    }

    @Test
    public void testThatAnnotatedPropertyIsSecond() {
        final String resolved = resolver.resolve(EntityWithIdFieldAndAnAnnotatedIdField.class, Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("annotatedId"));
    }

    @Test
    public void testThatNamedGetterIsThird() {
        final String resolved = resolver.resolve(EntityWithUnderscorePrecedingIdField.class, Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test
    public void testThatNamedFieldIsFourth() {
        final String resolved = resolver.resolve(EntityWithIdFieldHiddenBehindDifferentlyNamedAccessors.class,
                                                 Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test(expectedExceptions = NoIdPropertyException.class)
    public void testThatNoOtherValueIsHonored() {
        resolver.resolve(EntityWithNoImmediatelyResolvableIdProperty.class, Object.class);
    }

    /**
     * see https://github.com/mmnaseri/spring-data-mock/issues/83
     *
     */
    @Test(expectedExceptions = PrimitiveIdTypeException.class)
    public void testPrimitiveIdTypeDoesNotWork() {
        resolver.resolve(EntityWithPrimitiveIdProperty.class, Long.class);
    }

}