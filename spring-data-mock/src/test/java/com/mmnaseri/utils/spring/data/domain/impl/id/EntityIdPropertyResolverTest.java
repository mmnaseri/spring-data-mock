package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.error.NoIdPropertyException;
import com.mmnaseri.utils.spring.data.error.PrimitiveIdTypeException;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithAnnotationOnIdFieldAndGetterAndAnIdField;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithIdFieldAndAnAnnotatedIdField;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithIdFieldHiddenBehindDifferentlyNamedAccessors;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithNoImmediatelyResolvableIdProperty;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithPrimitiveIdProperty;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithUnderscorePrecedingIdField;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public class EntityIdPropertyResolverTest {

    private EntityIdPropertyResolver resolver;

    @BeforeMethod
    public void setUp() throws Exception {
        resolver = new EntityIdPropertyResolver();
    }

    @Test
    public void testThatAnnotatedGetterHasPrecedence() throws Exception {
        final String resolved = resolver.resolve(EntityWithAnnotationOnIdFieldAndGetterAndAnIdField.class, Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("unannotatedId"));
    }

    @Test
    public void testThatAnnotatedPropertyIsSecond() throws Exception {
        final String resolved = resolver.resolve(EntityWithIdFieldAndAnAnnotatedIdField.class, Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("annotatedId"));
    }

    @Test
    public void testThatNamedGetterIsThird() throws Exception {
        final String resolved = resolver.resolve(EntityWithUnderscorePrecedingIdField.class, Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test
    public void testThatNamedFieldIsFourth() throws Exception {
        final String resolved = resolver.resolve(EntityWithIdFieldHiddenBehindDifferentlyNamedAccessors.class, Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test(expectedExceptions = NoIdPropertyException.class)
    public void testThatNoOtherValueIsHonored() throws Exception {
        resolver.resolve(EntityWithNoImmediatelyResolvableIdProperty.class, Object.class);
    }

    /**
     * see https://github.com/mmnaseri/spring-data-mock/issues/83
     * @throws Exception
     */
    @Test(expectedExceptions = PrimitiveIdTypeException.class)
    public void testPrimitiveIdTypeDoesNotWork() throws Exception {
        resolver.resolve(EntityWithPrimitiveIdProperty.class,  Long.class);
    }

}