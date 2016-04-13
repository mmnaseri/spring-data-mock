package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.error.DataFunctionException;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public class DeleteDataFunction implements DataFunction<List<?>> {

    @Override
    public <K extends Serializable, E> List<E> apply(DataStore<K, E> dataStore, QueryDescriptor query, RepositoryConfiguration repositoryConfiguration, List<E> selection) {
        if (dataStore == null) {
            throw new InvalidArgumentException("Data store cannot be null");
        }
        if (query == null) {
            throw new InvalidArgumentException("Query cannot be null");
        }
        if (selection == null) {
            throw new InvalidArgumentException("Selection cannot be null");
        }
        final String identifier = query.getRepositoryMetadata().getIdentifierProperty();
        final List<E> deleted = new LinkedList<>();
        for (E item : selection) {
            final Object key;
            try {
                key = PropertyUtils.getPropertyValue(item, identifier);
            } catch (Exception e) {
                throw new DataFunctionException("Failed to read property value for property " + identifier, e);
            }
            if (key == null) {
                throw new DataFunctionException("Cannot delete an entity without the key property being set: " + identifier);
            }
            //noinspection unchecked
            if (dataStore.delete((K) key)) {
                deleted.add(item);
            }
        }
        return deleted;
    }

}
