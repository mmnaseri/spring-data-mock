package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
@SuppressWarnings("unchecked")
public class DefaultCrudRepository implements DataStoreAware, RepositoryMetadataAware, KeyGeneratorAware<Serializable> {

    private KeyGenerator<? extends Serializable> keyGenerator;
    private DataStore dataStore;
    private RepositoryMetadata repositoryMetadata;

    public Object save(Object entity) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        Object key = wrapper.getPropertyValue(repositoryMetadata.getIdentifierProperty());
        if (key == null && keyGenerator != null) {
            key = keyGenerator.generate();
            if (wrapper.isWritableProperty(repositoryMetadata.getIdentifierProperty())) {
                wrapper.setPropertyValue(repositoryMetadata.getIdentifierProperty(), key);
            } else {
                final Field field = ReflectionUtils.findField(repositoryMetadata.getEntityType(), repositoryMetadata.getIdentifierProperty());
                if (field != null) {
                    field.setAccessible(true);
                    try {
                        field.set(entity, key);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
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

    public Object delete(Serializable id) {
        final Object retrieved = dataStore.retrieve(id);
        dataStore.delete(id);
        return retrieved;
    }

    public Object delete(Object entity) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        final Object key = wrapper.getPropertyValue(repositoryMetadata.getIdentifierProperty());
        if (key == null) {
            throw new IllegalArgumentException("Entity must have a valid key: " + repositoryMetadata.getIdentifierProperty());
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

    @Override
    public void setKeyGenerator(KeyGenerator<Serializable> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

}
