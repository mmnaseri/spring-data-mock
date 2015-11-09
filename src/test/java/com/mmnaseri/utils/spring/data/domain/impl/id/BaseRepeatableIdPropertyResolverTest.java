package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import org.testng.annotations.Test;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class BaseRepeatableIdPropertyResolverTest extends BaseIdPropertyResolverTest {

    protected abstract Class<?> entityWithMultipleProperties();

    @Test(expectedExceptions = IllegalStateException.class)
    public void testFindingTheIdFieldOnEntityWithMultipleAnnotatedFields() throws Exception {
        final IdPropertyResolver resolver = getIdPropertyResolver();
        resolver.resolve(entityWithMultipleProperties(), Serializable.class);
    }

}
