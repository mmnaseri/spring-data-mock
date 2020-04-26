package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.DataStoreAware;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataAware;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.data.gemfire.repository.Wrapper;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/13/15)
 */
public class DefaultGemfireRepository implements DataStoreAware, RepositoryMetadataAware {

    private static final Log log = LogFactory.getLog(DefaultGemfireRepository.class);
    private DataStore dataStore;
    private RepositoryMetadata repositoryMetadata;

    /**
     * Saves the wrapped data object into the data store. If the wrapped object is also an instance of the type bound to
     * this data store, it will set the key on the object
     *
     * @param wrapper the wrapper for the key and the object
     * @return the saved entity
     */
    public Object save(Wrapper<Object, Object> wrapper) {
        log.info("Going to save a wrapped data store object");
        final Object entity = wrapper.getEntity();
        final Object key = wrapper.getKey();
        if (repositoryMetadata.getEntityType().isInstance(entity)) {
            log.debug("Since the entity is of the same type as the actual entity type supported by the data store, " +
                              "we know how to set the key on the wrapped entity.");
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
