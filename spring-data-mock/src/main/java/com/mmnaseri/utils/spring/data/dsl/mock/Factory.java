package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;

/**
 * Specifies the factory for the repository mock builder
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings({"WeakerAccess", "unused"})
public interface Factory {

    /**
     * Tells the builder to use the specified factory
     *
     * @param factory the factory
     * @return the rest of the configuration
     */
    KeyGeneration useFactory(RepositoryFactory factory);

}
