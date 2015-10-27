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
 * @since 1.0 (10/13/15)
 */
@SuppressWarnings("unchecked")
public class DefaultJpaRepository implements DataStoreAware, RepositoryMetadataAware, KeyGeneratorAware {

    private DataStore dataStore;
    private KeyGenerator keyGenerator;
    private RepositoryMetadata repositoryMetadata;

    public void flush() {
    }

    public Iterable deleteInBatch(Iterable entities) {
        final List result = new LinkedList();
        for (Object entity : entities) {
            final BeanWrapper wrapper = new BeanWrapperImpl(entity);
            final Object key = wrapper.getPropertyValue(repositoryMetadata.getIdentifier());
            if (key == null) {
                throw new IllegalArgumentException("Expected entity to have a key: " + entity);
            }
            final Serializable serializable = (Serializable) key;
            if (dataStore.hasKey(serializable)) {
                result.add(dataStore.retrieve(serializable));
                dataStore.delete(serializable);
            }
        }
        return result;
    }

    public Iterable deleteAllInBatch() {
        return deleteInBatch(dataStore.retrieveAll());
    }

    public Object getOne(Serializable serializable) {
        if (dataStore.hasKey(serializable)) {
            return dataStore.retrieve(serializable);
        }
        return null;
    }

    public Object saveAndFlush(Object entity) {
        final BeanWrapper wrapper = new BeanWrapperImpl(entity);
        final Object currentKey = wrapper.getPropertyValue(repositoryMetadata.getIdentifier());
        if (currentKey == null && keyGenerator != null) {
            final Serializable generated = keyGenerator.generate();
            if (wrapper.isWritableProperty(repositoryMetadata.getIdentifier())) {
                wrapper.setPropertyValue(repositoryMetadata.getIdentifier(), generated);
            } else {
                final Field field = ReflectionUtils.findField(repositoryMetadata.getEntityType(), repositoryMetadata.getIdentifier());
                if (field != null) {
                    field.setAccessible(true);
                    try {
                        field.set(entity, generated);
                    } catch (IllegalAccessException e) {
                        throw new IllegalStateException(e);
                    }
                }
            }
        }
        dataStore.save((Serializable) wrapper.getPropertyValue(repositoryMetadata.getIdentifier()), entity);
        flush();
        return entity;
    }

    @Override
    public void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public void setKeyGenerator(KeyGenerator keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public void setRepositoryMetadata(RepositoryMetadata repositoryMetadata) {
        this.repositoryMetadata = repositoryMetadata;
    }

}
