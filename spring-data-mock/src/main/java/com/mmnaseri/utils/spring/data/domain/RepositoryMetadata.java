package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface encapsulates metadata required from a repository for the rest of this framework to function.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/19/15)
 */
public interface RepositoryMetadata {

    /**
     * @return the name of the property that will yield the key to the entities represented by this repository
     */
    String getIdentifierProperty();

    /**
     * @return the type of the key this repository uses
     */
    Class<?> getIdentifierType();

    /**
     * @return the type of the entities this repository represents
     */
    Class<?> getEntityType();

    /**
     * @return the interface for the repository where actual methods will be defined
     */
    Class<?> getRepositoryInterface();

}
