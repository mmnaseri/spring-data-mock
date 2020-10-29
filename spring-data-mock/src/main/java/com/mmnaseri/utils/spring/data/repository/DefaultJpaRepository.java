package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.QueueingDataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/13/15)
 */
@SuppressWarnings({"unchecked", "WeakerAccess", "UnusedReturnValue"})
public class DefaultJpaRepository extends CrudRepositorySupport {

    private static final Log log = LogFactory.getLog(DefaultJpaRepository.class);

    /**
     * If the underlying data store supports {@link QueueingDataStore queueing} and needs the queue to be flushed, this
     * method will flush the queue. Otherwise, it will not do anything
     */
    public void flush() {
        final DataStore dataStore = getDataStore();
        if (dataStore instanceof QueueingDataStore) {
            log.info("Asking the data store to flush the underlying operation queue");
            final QueueingDataStore store = (QueueingDataStore) dataStore;
            store.flush();
        }
    }

    /**
     * Deletes the given entities by enclosing the actual delete in batch requests. If the underlying data store doesn't
     * support {@link QueueingDataStore queueing}, this will be no different than simply sequentially deleting all the
     * entities.
     *
     * @param entities entities to delete
     * @return deleted entities
     */
    public Iterable deleteInBatch(Iterable entities) {
        final List<Object> keys = new LinkedList<>();
        for (Object entity : entities) {
            final Object key = PropertyUtils.getPropertyValue(entity, getRepositoryMetadata().getIdentifierProperty());
            if (key == null) {
                log.error("There is no key set for the entity we were trying to delete");
                throw new EntityMissingKeyException(getRepositoryMetadata().getEntityType(),
                                                    getRepositoryMetadata().getIdentifierProperty());
            }
            keys.add(key);
        }
        return deleteByKeysInBatch(keys);
    }

    /**
     * Deletes everything in the data store that's of the bound entity type
     *
     * @return deleted entities
     */
    public Iterable deleteAllInBatch() {
        log.info("Attempting to delete all the entities in the data store in a batch");
        return deleteByKeysInBatch(getDataStore().keys());
    }

    /**
     * Deletes entities bound to the passed keys in batch
     *
     * @param keys the keys
     * @return deleted entities
     */
    private Iterable deleteByKeysInBatch(Collection<Object> keys) {
        final Object batch;
        if (getDataStore() instanceof QueueingDataStore) {
            log.debug("The data store support queueing, so we are going to start a batch");
            batch = ((QueueingDataStore) getDataStore()).startBatch();
        } else {
            log.debug("The data store does not support batching, so this is the same as a normal delete");
            batch = null;
        }
        final List result = new LinkedList();
        for (Object key : keys) {
            if (getDataStore().hasKey(key)) {
                result.add(getDataStore().retrieve(key));
                getDataStore().delete(key);
            }
        }
        if (getDataStore() instanceof QueueingDataStore) {
            log.debug("Ending the batch operation that was started previously.");
            ((QueueingDataStore) getDataStore()).endBatch(batch);
        }
        return result;
    }

    /**
     * Returns the entity that has the given key
     *
     * @param key the key
     * @return returns the entity or {@literal null} if it couldn't be found
     */
    public Object getOne(Object key) {
        if (getDataStore().hasKey(key)) {
            log.info("Returning entity for key " + key);
            return getDataStore().retrieve(key);
        }
        log.info("No entity was found with key " + key);
        return null;
    }

    /**
     * Saves the entity to the database and flushes the queue
     *
     * @param entity the entity
     * @return the saved entity
     */
    public Object saveAndFlush(Object entity) {
        log.info("Saving entity " + entity + " to the data store.");
        final Object saved = save(entity);
        log.info("Going to flush the data store after the save");
        flush();
        return saved;
    }

}
