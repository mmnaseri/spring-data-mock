package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.store.QueueingDataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.io.Serializable;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/13/15)
 */
@SuppressWarnings("unchecked")
public class DefaultJpaRepository extends CrudRepositorySupport {

    public void flush() {
        final DataStore dataStore = getDataStore();
        if (dataStore instanceof QueueingDataStore) {
            final QueueingDataStore store = (QueueingDataStore) dataStore;
            store.flush();
        }
    }

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
        return deleteByKeys(keys);
    }

    public Iterable deleteAllInBatch() {
        return deleteByKeys(getDataStore().keys());
    }

    private Iterable deleteByKeys(Collection<Serializable> keys) {
        final List result = new LinkedList();
        for (Serializable key : keys) {
            if (getDataStore().hasKey(key)) {
                result.add(getDataStore().retrieve(key));
                getDataStore().delete(key);
            }
        }
        return result;
    }

    public Object getOne(Serializable serializable) {
        if (getDataStore().hasKey(serializable)) {
            return getDataStore().retrieve(serializable);
        }
        return null;
    }

    public Object saveAndFlush(Object entity) {
        final Object saved = save(entity);
        flush();
        return saved;
    }

}
