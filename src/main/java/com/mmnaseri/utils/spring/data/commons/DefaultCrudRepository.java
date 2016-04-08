package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.error.EntityMissingKeyException;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
@SuppressWarnings("unchecked")
public class DefaultCrudRepository extends AbstractCrudRepository {

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
            entities.add(findOne((Serializable) id));
        }
        return entities;
    }

    public Object delete(Serializable id) {
        final Object retrieved = getDataStore().retrieve(id);
        getDataStore().delete(id);
        return retrieved;
    }

    public Object delete(Object entity) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        final Object key = wrapper.getPropertyValue(getRepositoryMetadata().getIdentifierProperty());
        if (key == null) {
            throw new EntityMissingKeyException(getRepositoryMetadata().getEntityType(), getRepositoryMetadata().getIdentifierProperty());
        }
        return delete((Serializable) key);
    }

    public Iterable delete(Iterable entities) {
        final List list = new LinkedList();
        for (Object entity : entities) {
            list.add(delete(entity));
        }
        return list;
    }

    public Iterable deleteAll() {
        return delete(getDataStore().retrieveAll());
    }

}
