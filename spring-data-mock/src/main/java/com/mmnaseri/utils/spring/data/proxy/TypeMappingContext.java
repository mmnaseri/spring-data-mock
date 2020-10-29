package com.mmnaseri.utils.spring.data.proxy;

import java.util.List;

/**
 * This interface encapsulates a context for holding mappings for a given repository type.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public interface TypeMappingContext {

    /**
     * Registers a mapping that should be honored for all repositories that are a subtype of the provided repository
     * type. This means that if you register an implementation for {@link Object} all repositories will inherit this
     * mapping.
     *
     * @param repositoryType the repository (super) type
     * @param implementation the implementation type
     */
    void register(Class<?> repositoryType, Class<?> implementation);

    /**
     * Given a repository type returns all concrete classes that could be used for that repository's mappings
     *
     * @param repositoryType the repository type
     * @return the list of all possible mappings
     */
    List<Class<?>> getImplementations(Class<?> repositoryType);

    /**
     * Given a repository type, will look up all possible mappings and creates {@link TypeMapping mapping} objects from
     * those
     *
     * @param repositoryType the repository type
     * @return a list of applicable mappings in the order in which they were registered
     */
    List<TypeMapping<?>> getMappings(Class<?> repositoryType);

}
