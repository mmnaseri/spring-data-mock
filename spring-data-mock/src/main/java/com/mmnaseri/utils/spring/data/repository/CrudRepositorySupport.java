package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * <p>This implementation is used to factor out the commonalities between various Spring interfaces extending the
 * {@link org.springframework.data.repository.CrudRepository} interface.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/11/09, 21:40)
 */
@SuppressWarnings("WeakerAccess")
public class CrudRepositorySupport implements DataStoreAware, RepositoryMetadataAware, KeyGeneratorAware<Object> {

    private static final Log log = LogFactory.getLog(CrudRepositorySupport.class);
    private KeyGenerator<?> keyGenerator;
    private DataStore dataStore;
    private RepositoryMetadata repositoryMetadata;

    protected CrudRepositorySupport() {
    }

    /**
     * Saves the entity in the underlying data store, creating keys in the process, if necessary.
     *
     * @param entity the entity to save
     * @return the saved entity (the exact same instance, with the difference that if the entity was newly inserted, it
     * will have a key).
     */
    public Object save(Object entity) {
        Object key = PropertyUtils.getPropertyValue(entity, repositoryMetadata.getIdentifierProperty());
        log.info("The entity that is to be saved has a key with value " + key);
        if (key == null && keyGenerator != null) {
            log.info("The key was null, but the generator was not, so we are going to get a key for the entity");
            key = keyGenerator.generate();
            log.debug("The generated key for the entity was " + key);
            PropertyUtils.setPropertyValue(entity, repositoryMetadata.getIdentifierProperty(), key);
        }
        if (key == null) {
            log.warn(
                    "Attempting to save an entity without a key. This might result in an error. To fix this, specify "
                            + "a key generator.");
        }
        //noinspection unchecked
        dataStore.save(key, entity);
        return entity;
    }

    @Override
    public final void setDataStore(DataStore dataStore) {
        this.dataStore = dataStore;
    }

    @Override
    public final void setKeyGenerator(KeyGenerator<?> keyGenerator) {
        this.keyGenerator = keyGenerator;
    }

    @Override
    public final void setRepositoryMetadata(RepositoryMetadata repositoryMetadata) {
        this.repositoryMetadata = repositoryMetadata;
    }

    protected KeyGenerator<?> getKeyGenerator() {
        return keyGenerator;
    }

    protected DataStore getDataStore() {
        return dataStore;
    }

    protected RepositoryMetadata getRepositoryMetadata() {
        return repositoryMetadata;
    }

}
