package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;

/**
 * Lets us configure the underlying factory using a configuration object
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface Configuration {

    /**
     * Tells the builder to use the given configuration
     * @param configuration    the configuration
     * @return the rest of the configuration
     */
    KeyGeneration useConfiguration(RepositoryFactoryConfiguration configuration);

}
