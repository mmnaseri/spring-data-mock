package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.sample.models.EmptyEntity;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithIdField;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
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
        return EmptyEntity.class;
    }


}