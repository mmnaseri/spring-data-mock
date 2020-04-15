package com.mmnaseri.utils.spring.data.store;

import java.util.Collection;

/**
 * This interface encapsulates the abstract data store, with the bare minimum capabilities assumed.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public interface DataStore<K, E> {

    /**
     * Determines whether or not an entity with the given key exists in the data store.
     * @param key    the key
     * @return {@literal boolean} if such a key is known by the data store
     */
    boolean hasKey(K key);

    /**
     * Saves the given entity into the data store under the provided key. Whether or not the key is actually used by the
     * data store in some sort of mapping does not matter. The only expectation is that after this call, should the
     * save be successful a call to {@link #hasKey(Object)} should yield {@literal true}
     * and the very same entity (or an equal value entity) can be retrieved by calling {@link #retrieve(Object)}.
     * @param key       the key
     * @param entity    the entity
     * @return {@literal true} to indicate the entity was a new entry, and {@literal false} to indicate another entity
     * had to be replaced/updated with the provided entity
     */
    boolean save(K key, E entity);

    /**
     * Deletes the entity identifiable with the provided key, or does nothing if no such entity exists. It is expected
     * that as a side effect, once this method returns successfully, {@link #hasKey(Object)} should return
     * {@literal false} and {@link #retrieve(Object)} should return {@literal null} for the same key.
     * @param key    the key for which the removal should happen
     * @return {@literal true} to indicate the entity was located and removed and {@literal false} to indicate that
     * no such entity existed in the first place.
     */
    boolean delete(K key);

    /**
     * Given a key, retrieves the entity associated with that key in the data store.
     * @param key    the key
     * @return the entity or {@literal null} if no such entity could be found
     */
    E retrieve(K key);

    /**
     * @return a collection of all the keys registered in the data store.
     */
    Collection<K> keys();

    /**
     * @return retrieves all the entities in the data store
     */
    Collection<E> retrieveAll();

    /**
     * @return the entity type bound to this data store
     */
    Class<E> getEntityType();

    /**
     * Does a hard remove of all the entities in the data store
     */
    void truncate();

}
