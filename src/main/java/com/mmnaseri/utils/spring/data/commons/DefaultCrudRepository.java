package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataAware;
import com.mmnaseri.utils.spring.data.store.DataStore;
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
public class DefaultCrudRepository implements DataStoreAware, RepositoryMetadataAware {

    private DataStore dataStore;
    private RepositoryMetadata repositoryMetadata;

    private Object generateKey() {
        return null;
    }

    public Object save(Object entity) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        Object key = wrapper.getPropertyValue(repositoryMetadata.getIdentifier());
        if (key == null) {
            key = generateKey();
            wrapper.setPropertyValue(repositoryMetadata.getIdentifier(), key);
        }
        dataStore.save((Serializable) key, entity);
        return entity;
    }

    public Iterable<Object> save(Iterable entities) {
        final List<Object> list = new LinkedList<Object>();
        for (Object entity : entities) {
            list.add(save(entity));
        }
        return list;
    }

    public Object findOne(Serializable key) {
        return dataStore.retrieve(key);
    }

    public boolean exists(Serializable key) {
        return dataStore.hasKey(key);
    }

    public Iterable findAll() {
        return dataStore.retrieveAll();
    }

    public Iterable findAll(Iterable ids) {
        final List entities = new LinkedList();
        for (Object id : ids) {
            entities.add(findOne((Serializable) id));
        }
        return entities;
    }

    public long count() {
        return dataStore.retrieveAll().size();
    }

    public Object delete(Serializable id) {
        final Object retrieved = dataStore.retrieve(id);
        dataStore.delete(id);
        return retrieved;
    }

    public Object delete(Object entity) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        final Object key = wrapper.getPropertyValue(repositoryMetadata.getIdentifier());
        if (key == null) {
            throw new IllegalArgumentException("Entity must have a valid key: " + repositoryMetadata.getIdentifier());
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
        return delete(dataStore.retrieveAll());
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void setRepositoryMetadata(RepositoryMetadata repositoryMetadata) {
        this.repositoryMetadata = repositoryMetadata;
    }

}
