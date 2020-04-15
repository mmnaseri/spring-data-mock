package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public abstract class BaseIdPropertyResolverTest {

    protected abstract IdPropertyResolver getIdPropertyResolver();

    protected abstract Class<?> properEntity();

    protected abstract Class<?> entityWithNoProperty();

    @Test(expectedExceptions = PropertyTypeMismatchException.class)
    public void testFindingTheIdFieldWithWrongType() throws Exception {
        final IdPropertyResolver resolver = getIdPropertyResolver();
        resolver.resolve(properEntity(), Long.class);
    }

    @Test
    public void testFindingTheIdFieldWithSuperType() throws Exception {
        final IdPropertyResolver resolver = getIdPropertyResolver();
        final String resolved = resolver.resolve(properEntity(), Object.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test
    public void testFindingTheIdFieldWithCorrectType() throws Exception {
        final IdPropertyResolver resolver = getIdPropertyResolver();
        final String resolved = resolver.resolve(properEntity(), String.class);
        assertThat(resolved, is(notNullValue()));
        assertThat(resolved, is("id"));
    }

    @Test
    public void testFindingTheIdFieldOnEntityWithoutAnnotations() throws Exception {
        final IdPropertyResolver resolver = getIdPropertyResolver();
        final String resolved = resolver.resolve(entityWithNoProperty(), Object.class);
        assertThat(resolved, is(nullValue()));
    }
}
