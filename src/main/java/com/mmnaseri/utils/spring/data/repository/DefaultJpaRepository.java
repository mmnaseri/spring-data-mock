package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.QueueingDataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/13/15)
 */
@SuppressWarnings({"unchecked", "WeakerAccess"})
public class DefaultJpaRepository extends CrudRepositorySupport {

    /**
     * If the underlying data store supports {@link QueueingDataStore queueing} and needs the queue to be
     * flushed, this method will flush the queue. Otherwise, it will not do anything
     */
    public void flush() {
        final DataStore dataStore = getDataStore();
        if (dataStore instanceof QueueingDataStore) {
            final QueueingDataStore store = (QueueingDataStore) dataStore;
            store.flush();
        }
    }

    /**
     * Deletes the given entities by enclosing the actual delete in batch requests. If the underlying data store
     * doesn't support {@link QueueingDataStore queueing}, this will be no different than simply sequentially
     * deleting all the entities.
     * @param entities    entities to delete
     * @return deleted entities
     */
    public Iterable deleteInBatch(Iterable entities) {
        final List<Serializable> keys = new LinkedList<>();
        for (Object entity : entities) {
            final Object key = PropertyUtils.getPropertyValue(entity, getRepositoryMetadata().getIdentifierProperty());
            if (key == null) {
                throw new EntityMissingKeyException(getRepositoryMetadata().getEntityType(), getRepositoryMetadata().getIdentifierProperty());
            }
            final Serializable serializable = (Serializable) key;
            keys.add(serializable);
        }
        return deleteByKeysInBatch(keys);
    }

    /**
     * Deletes everything in the data store that's of the bound entity type
     * @return deleted entities
     */
    public Iterable deleteAllInBatch() {
        return deleteByKeysInBatch(getDataStore().keys());
    }

    /**
     * Deletes entities bound to the passed keys in batch
     * @param keys    the keys
     * @return deleted entities
     */
    private Iterable deleteByKeysInBatch(Collection<Serializable> keys) {
        final Object batch;
        if (getDataStore() instanceof QueueingDataStore) {
            batch = ((QueueingDataStore) getDataStore()).startBatch();
        } else {
            batch = null;
        }
        final List result = new LinkedList();
        for (Serializable key : keys) {
            if (getDataStore().hasKey(key)) {
                result.add(getDataStore().retrieve(key));
                getDataStore().delete(key);
            }
        }
        if (getDataStore() instanceof QueueingDataStore) {
            ((QueueingDataStore) getDataStore()).endBatch(batch);
        }
        return result;
    }

    /**
     * Returns the entity that has the given key
     * @param key    the key
     * @return returns the entity or {@literal null} if it couldn't be found
     */
    public Object getOne(Serializable key) {
        if (getDataStore().hasKey(key)) {
            return getDataStore().retrieve(key);
        }
        return null;
    }

    /**
     * Saves the entity to the database and flushes the queue
     * @param entity    the entity
     * @return the saved entity
     */
    public Object saveAndFlush(Object entity) {
        final Object saved = save(entity);
        flush();
        return saved;
    }

}
