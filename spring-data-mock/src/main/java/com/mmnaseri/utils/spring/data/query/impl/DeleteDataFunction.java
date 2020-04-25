package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.error.DataFunctionException;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.QueryDescriptor;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;

/**
 * This function provides support for the delete data operation, by issuing a delete request for every selected entity
 * to the underlying data store.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
@SuppressWarnings("WeakerAccess")
public class DeleteDataFunction implements DataFunction<List<?>> {

    private static final Log log = LogFactory.getLog(DeleteDataFunction.class);

    @Override
    public <K, E> List<E> apply(DataStore<K, E> dataStore, QueryDescriptor query,
                                RepositoryConfiguration repositoryConfiguration, List<E> selection) {
        if (dataStore == null) {
            log.error("Cannot delete entities when the data store is null");
            throw new InvalidArgumentException("Data store cannot be null");
        }
        if (query == null) {
            log.error("Cannot delete entities when the query is null");
            throw new InvalidArgumentException("Query cannot be null");
        }
        if (selection == null) {
            log.error("Cannot delete entities when the selection is null");
            throw new InvalidArgumentException("Selection cannot be null");
        }
        final String identifier = query.getRepositoryMetadata().getIdentifierProperty();
        log.info("Using property " + identifier + " to delete the entities");
        final List<E> deleted = new LinkedList<>();
        for (E item : selection) {
            final Object key;
            try {
                key = PropertyUtils.getPropertyValue(item, identifier);
            } catch (Exception e) {
                log.error("The value of property " + identifier + " could not be read ");
                throw new DataFunctionException("Failed to read property value for property " + identifier, e);
            }
            if (key == null) {
                log.error("Cannot delete an entity when the identifier property has been set to null");
                throw new DataFunctionException(
                        "Cannot delete an entity without the key property being set: " + identifier);
            }
            //noinspection unchecked
            if (dataStore.delete((K) key)) {
                deleted.add(item);
            }
        }
        log.error("Deleted " + deleted.size() + " entities as the result of the delete call");
        return deleted;
    }

}
