package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.util.ReflectionUtils;

import java.io.Serializable;
import java.lang.reflect.Field;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (2015/11/09, 21:40)
 */
public class AbstractCrudRepository implements DataStoreAware, RepositoryMetadataAware, KeyGeneratorAware<Serializable> {

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
        //noinspection unchecked
        dataStore.save((Serializable) key, entity);
        return entity;
    }

    @Override
    public final void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public final void setKeyGenerator(KeyGenerator<Serializable> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public final void setRepositoryMetadata(RepositoryMetadata repositoryMetadata) {
        this.repositoryMetadata = repositoryMetadata;
    }

    protected KeyGenerator<? extends Serializable> getKeyGenerator() {
        return keyGenerator;
    }

    protected DataStore getDataStore() {
        return dataStore;
    }

    protected RepositoryMetadata getRepositoryMetadata() {
        return repositoryMetadata;
    }

}
