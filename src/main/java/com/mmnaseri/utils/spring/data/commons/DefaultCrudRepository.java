package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
@SuppressWarnings("unchecked")
public class DefaultCrudRepository extends CrudRepositorySupport {

    public Iterable<Object> save(Iterable entities) {
        final List<Object> list = new LinkedList<Object>();
        for (Object entity : entities) {
            list.add(save(entity));
        }
        return list;
    }

    public Object findOne(Serializable key) {
        return getDataStore().retrieve(key);
    }

    public boolean exists(Serializable key) {
        return getDataStore().hasKey(key);
    }

    public Iterable findAll() {
        return getDataStore().retrieveAll();
    }

    public Iterable findAll(Iterable ids) {
        final List entities = new LinkedList();
        for (Object id : ids) {
            final Object found = findOne((Serializable) id);
            if (found != null) {
                entities.add(found);
            }
        }
        return entities;
    }

    public Object delete(Serializable id) {
        final Object retrieved = getDataStore().retrieve(id);
        getDataStore().delete(id);
        return retrieved;
    }

    public Object delete(Object entity) {
        final Object key = PropertyUtils.getPropertyValue(entity, getRepositoryMetadata().getIdentifierProperty());
        if (key == null) {
            throw new EntityMissingKeyException(getRepositoryMetadata().getEntityType(), getRepositoryMetadata().getIdentifierProperty());
        }
        return delete((Serializable) key);
    }

    public Iterable delete(Iterable entities) {
        final List list = new LinkedList();
        for (Object entity : entities) {
            final Object deleted = delete(entity);
            if (deleted != null) {
                list.add(deleted);
            }
        }
        return list;
    }

    public Iterable deleteAll() {
        final List list = new LinkedList();
        for (Object key : getDataStore().keys()) {
            final Object deleted = delete(((Serializable) key));
            if (deleted != null) {
                list.add(deleted);
            }
        }
        return list;
    }

}
