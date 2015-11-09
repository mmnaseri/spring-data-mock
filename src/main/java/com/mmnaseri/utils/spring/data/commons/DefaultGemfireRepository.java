package com.mmnaseri.utils.spring.data.commons;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataAware;
import com.mmnaseri.utils.spring.data.store.DataStore;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.gemfire.repository.Wrapper;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/13/15)
 */
public class DefaultGemfireRepository implements DataStoreAware, RepositoryMetadataAware {

    private DataStore dataStore;
    private RepositoryMetadata repositoryMetadata;

    public Object save(Wrapper<Object, Serializable> wrapper) {
        final Object entity = wrapper.getEntity();
        final Serializable key = wrapper.getKey();
        final BeanWrapper beanWrapper = new BeanWrapperImpl(entity);
        beanWrapper.setPropertyValue(repositoryMetadata.getIdentifierProperty(), key);
        //noinspection unchecked
        dataStore.save(key, entity);
        return entity;
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
