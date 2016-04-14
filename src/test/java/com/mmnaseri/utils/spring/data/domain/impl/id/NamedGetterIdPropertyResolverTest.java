package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.sample.models.EmptyEntity;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithoutAnnotatedIdGetter;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("unused")
public class NamedGetterIdPropertyResolverTest extends BaseIdPropertyResolverTest {

    @Override
    protected IdPropertyResolver getIdPropertyResolver() {
        return new NamedGetterIdPropertyResolver();
    }

    @Override
    protected Class<?> properEntity() {
        return EntityWithoutAnnotatedIdGetter.class;
    }

    @Override
    protected Class<?> entityWithNoProperty() {
        return EmptyEntity.class;
    }

}