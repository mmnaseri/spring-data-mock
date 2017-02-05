package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * <p>
 * This class will provide implementations for the methods introduced by the
 * Spring framework through
 * {@link org.springframework.data.repository.CrudRepository}.</p>
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/6/15)
 */
@SuppressWarnings({"unchecked", "WeakerAccess"})
public class DefaultCrudRepository extends CrudRepositorySupport {

    private static final Log log = LogFactory.getLog(DefaultCrudRepository.class);

    /**
     * Saves all the given entities
     * @param entities entities to save (insert or update)
     * @return saved entities
     */
    public Iterable<Object> save(Iterable entities) {
        final List<Object> list = new LinkedList<>();
        log.info("Going to save a number of entities in the underlying data store");
        log.debug(entities);
        for (Object entity : entities) {
            list.add(save(entity));
        }
        return list;
    }

    /**
     * Finds the entity that was saved with this key, or returns {@literal null}
     * @param key the key
     * @return the entity
     */
    public Object findOne(Serializable key) {
        log.info("Attempting to load the entity with key " + key);
        return getDataStore().retrieve(key);
    }

    /**
     * Checks whether the given key represents an entity in the data store
     * @param key the key
     * @return {@literal true} if the key is valid
     */
    public boolean exists(Serializable key) {
        return getDataStore().hasKey(key);
    }

    /**
     * Finds all the entities that match the given set of ids
     * @param ids ids to look for
     * @return entities that matched the ids.
     */
    public Iterable findAll(Iterable ids) {
        final List entities = new LinkedList();
        log.info("Looking for multiple entities for a number of ids");
        log.debug(ids);
        for (Object id : ids) {
            final Object found = findOne((Serializable) id);
            if (found != null) {
                log.trace("Entity found for key " + id + ", adding the found entity to the list of returned entity");
                entities.add(found);
            }
        }
        return entities;
    }

    /**
     * Deletes the entity with the given id and returns the actual entity that
     * was just deleted.
     * @param id the id
     * @return the entity that was deleted or {@literal null} if it wasn't found
     */
    public Object delete(Serializable id) {
        Object retrieved = getDataStore().retrieve(id);
        log.info("Attempting to delete the entity with key " + id);
        if (retrieved == null) {
            log.info("Object not found with key " + id + ", try to find by identifier property");
            try {
                id = (Serializable) PropertyUtils.getPropertyValue(id, getRepositoryMetadata().getIdentifierProperty());
                retrieved = getDataStore().retrieve(id);
            } catch (IllegalStateException exception) {
                log.info("Serialized id doesn't have a identifier property");
            }
        }
        getDataStore().delete(id);
        return retrieved;
    }

    /**
     * Deletes the entity matching this entity's key from the data store
     * @param entity the entity
     * @return the deleted entity
     * @throws EntityMissingKeyException if the passed entity doesn't have a key
     */
    public Object delete(Object entity) {
        final Object key = PropertyUtils.getPropertyValue(entity, getRepositoryMetadata().getIdentifierProperty());
        if (key == null) {
            log.error("The entity that was supposed to be deleted, does not have a key");
            throw new EntityMissingKeyException(getRepositoryMetadata().getEntityType(), getRepositoryMetadata().getIdentifierProperty());
        }
        return delete((Serializable) key);
    }

    /**
     * Deletes all specified <em>entities</em> from the data store.
     * @param entities the entities to delete
     * @return the entities that were actually deleted
     */
    public Iterable delete(Iterable entities) {
        log.info("Attempting to delete multiple entities via entity objects themselves");
        log.debug(entities);
        final List list = new LinkedList();
        for (Object entity : entities) {
            final Object deleted = delete(entity);
            if (deleted != null) {
                log.debug("The entity was deleted successfully and will be added to the list of deleted entities");
                list.add(deleted);
            }
        }
        return list;
    }

    /**
     * Deletes everything from the data store
     * @return all the entities that were removed
     */
    public Iterable deleteAll() {
        log.info("Attempting to delete all entities at once");
        final List list = new LinkedList();
        final Collection keys = getDataStore().keys();
        log.debug("There are " + keys.size() + " entities altogether in the data store that are going to be deleted");
        for (Object key : keys) {
            final Object deleted = delete(((Serializable) key));
            if (deleted != null) {
                log.debug("The entity was deleted successfully and will be added to the list of deleted entities");
                list.add(deleted);
            }
        }
        final Collection remainingKeys = getDataStore().keys();
        log.debug("There are " + remainingKeys.size() + " keys remaining in the data store after the delete operation");
        return list;
    }

}
