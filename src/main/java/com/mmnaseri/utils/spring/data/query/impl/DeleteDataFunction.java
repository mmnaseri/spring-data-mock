package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.io.Serializable;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class DeleteDataFunction implements DataFunction<List<?>> {

    @Override
    public <K extends Serializable, E> List<E> apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration repositoryConfiguration, List<E> selection) {
        final String identifier = query.getRepositoryMetadata().getIdentifierProperty();
        for (E item : selection) {
            final Object key = PropertyUtils.getPropertyValue(item, identifier);
            if (key == null) {
                throw new IllegalStateException("Cannot delete an entity without the key property being set: " + identifier);
            }
            //noinspection unchecked
            dataStore.delete((K) key);
        }
        return selection;
    }

}
