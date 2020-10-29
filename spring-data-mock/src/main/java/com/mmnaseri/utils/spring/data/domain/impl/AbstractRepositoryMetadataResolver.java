package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.IdPropertyResolver;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadataResolver;
import com.mmnaseri.utils.spring.data.domain.impl.id.EntityIdPropertyResolver;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;

import java.lang.reflect.Modifier;

/**
 * This class will first check for common errors in a repository's definition before letting the
 * resolution proceed any further.
 *
 * <p>A repository must:
 *
 * <ul>
 *   <li>Not be {@literal null}
 *   <li>Be an interface
 *   <li>Be declared as {@literal public}
 * </ul>
 *
 * <p>Once these checks are completed, it will call {@link #resolveFromInterface(Class)} to
 * determine the actual metadata.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/23/15)
 */
public abstract class AbstractRepositoryMetadataResolver implements RepositoryMetadataResolver {

  private final IdPropertyResolver idPropertyResolver;

  protected AbstractRepositoryMetadataResolver() {
    idPropertyResolver = new EntityIdPropertyResolver();
  }

  @Override
  public final RepositoryMetadata resolve(Class<?> repositoryInterface) {
    if (repositoryInterface == null) {
      throw new RepositoryDefinitionException(null, "Repository interface must not be null");
    }
    if (!Modifier.isInterface(repositoryInterface.getModifiers())) {
      throw new RepositoryDefinitionException(
          repositoryInterface,
          "Cannot resolve repository metadata for a class object that isn't" + " an interface");
    }
    if (!Modifier.isPublic(repositoryInterface.getModifiers())) {
      throw new RepositoryDefinitionException(
          repositoryInterface, "Repository interface needs to be declared as public");
    }
    return resolveFromInterface(repositoryInterface);
  }

  /**
   * Determines the metadata from the given repository interface, knowing that the assumptions
   * declared {@link AbstractRepositoryMetadataResolver the head of this class} now hold.
   *
   * @param repositoryInterface the repository interface.
   * @return the resolved metadata for the given repository interface.
   */
  protected abstract RepositoryMetadata resolveFromInterface(Class<?> repositoryInterface);

  /**
   * Given the type of the entity, it will determine the ID property for that entity through calling
   * to {@link EntityIdPropertyResolver}
   *
   * @param entityType the type of the entity
   * @param idType the type of the ID property
   * @return the name of the ID property
   */
  @SuppressWarnings("WeakerAccess")
  protected String resolveIdProperty(Class<?> entityType, Class<?> idType) {
    return idPropertyResolver.resolve(entityType, idType);
  }
}
