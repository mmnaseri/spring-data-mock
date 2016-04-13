package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataAware;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.springframework.data.gemfire.repository.Wrapper;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/13/15)
 */
public class DefaultGemfireRepository implements DataStoreAware, RepositoryMetadataAware {

    private DataStore dataStore;
    private RepositoryMetadata repositoryMetadata;

    /**
     * Saves the wrapped data object into the data store. If the wrapped object is also an instance of
     * the type bound to this data store, it will set the key on the object
     * @param wrapper    the wrapper for the key and the object
     * @return the saved entity
     */
    public Object save(Wrapper<Object, Serializable> wrapper) {
        final Object entity = wrapper.getEntity();
        final Serializable key = wrapper.getKey();
        if (repositoryMetadata.getEntityType().isInstance(entity)) {
            PropertyUtils.setPropertyValue(entity, repositoryMetadata.getIdentifierProperty(), key);
        }
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
