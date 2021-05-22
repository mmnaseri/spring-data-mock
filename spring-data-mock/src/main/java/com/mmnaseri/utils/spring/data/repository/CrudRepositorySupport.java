package com.mmnaseri.utils.spring.data.repository;

import com.mmnaseri.utils.spring.data.domain.*;
import com.mmnaseri.utils.spring.data.store.DataStore;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.util.LinkedList;
import java.util.List;
/**
 * This implementation is used to factor out the commonalities between various Spring interfaces
 * extending the {@link org.springframework.data.repository.CrudRepository} interface.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/11/09, 21:40)
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public class CrudRepositorySupport
    implements DataStoreAware, RepositoryMetadataAware, KeyGeneratorAware<Object> {

  private static final Log log = LogFactory.getLog(CrudRepositorySupport.class);
  private KeyGenerator<?> keyGenerator;
  private KeyGenerationStrategy keyGenerationStrategy = KeyGenerationStrategy.ONLY_NULL;
  private DataStore dataStore;
  private RepositoryMetadata repositoryMetadata;

  protected CrudRepositorySupport() {}

  /**
   * Saves the entity in the underlying data store, creating keys in the process, if necessary.
   *
   * @param entity the entity to save
   * @return the saved entity (the exact same instance, with the difference that if the entity was
   *     newly inserted, it will have a key).
   */
  public Object save(Object entity) {
    if (entity instanceof Iterable) {
      return save((Iterable) entity);
    }
    Object key = PropertyUtils.getPropertyValue(entity, repositoryMetadata.getIdentifierProperty());
    log.info("The entity that is to be saved has a key with value " + key);
    if (needGenerateKey(entity, key) && keyGenerator != null) {
      log.info(
          "The key was null or entity is not managed, but the generator was not, so we are going to get a key for the entity");
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

  /**
   * Saves all the given entities
   *
   * @param entities entities to save (insert or update)
   * @return saved entities
   */
  public Iterable<Object> save(Iterable entities) {
    final List<Object> list = new LinkedList<>();
    log.info("Going to save a number of entities in the underlying data store");
    log.debug(entities);
    for (Object entity : entities) {
      list.add(save(entity));
    }
    return list;
  }

  /**
   * Inserts the given entity via the {@link #save(Object)} method.
   *
   * @param entity the entity to be inserted.
   * @return the saved entity. This should result in the entity having a key.
   */
  public Object insert(Object entity) {
    return save(entity);
  }

  protected KeyGenerator<?> getKeyGenerator() {
    return keyGenerator;
  }

  @Override
  public final void setKeyGenerator(KeyGenerator<?> keyGenerator) {
    this.keyGenerator = keyGenerator;
  }

  @Override
  public void setKeyGenerationStrategy(KeyGenerationStrategy strategy) {
    this.keyGenerationStrategy = strategy;
  }

  protected DataStore getDataStore() {
    return dataStore;
  }

  @Override
  public final void setDataStore(DataStore dataStore) {
    this.dataStore = dataStore;
  }

  protected RepositoryMetadata getRepositoryMetadata() {
    return repositoryMetadata;
  }

  @Override
  public final void setRepositoryMetadata(RepositoryMetadata repositoryMetadata) {
    this.repositoryMetadata = repositoryMetadata;
  }

  private boolean needGenerateKey(Object entity, Object key) {
    return null == key ||
            KeyGenerationStrategy.ALL_UNMANAGED == keyGenerationStrategy && !isManaged(entity);
  }

  private boolean isManaged(Object entity) {
    return dataStore.retrieveAll().contains(entity);
  }
}
