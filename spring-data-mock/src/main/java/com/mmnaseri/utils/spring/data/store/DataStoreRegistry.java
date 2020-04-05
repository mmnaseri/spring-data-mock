package com.mmnaseri.utils.spring.data.store;

/**
 * This interface is used to register and look up data store for specific entity types.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface DataStoreRegistry {

    /**
     * Registers a new data store. Note that you can override an existing data store for an entity by registering
     * another one that supports the same sort of entity.
     * @param dataStore    the data store
     * @param <E>          the type of the entities
     * @param <K>          the type of the keys
     */
    <E, K> void register(DataStore<K, E> dataStore);

    /**
     * Finds the data store for the given entity type
     * @param entityType    the entity type
     * @param <E>          the type of the entities
     * @param <K>          the type of the keys
     * @return the data store that can handle the provided type of entity
     * @throws com.mmnaseri.utils.spring.data.error.DataStoreNotFoundException if no data store can be found
     * for the given entity type
     */
    <E, K> DataStore<K, E> getDataStore(Class<E> entityType);

    /**
     * Used to determine whether or not a data store has been registered that supports the given entity type.
     * @param entityType    the entity type
     * @return {@literal true} to indicate that the entity type has a corresponding data store in this registry
     */
    boolean has(Class<?> entityType);

}
