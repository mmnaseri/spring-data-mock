package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.dsl.mock.KeyGeneration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;

/**
 * Finalizes the DSL by providing a way to either choose to {@link #build() build} the factory or to
 * {@link KeyGeneration continue} with the DSL and mock a repository instead, thus complementing the
 * grammar for the repository factory build DSL with that of the repository mock builder.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface End extends KeyGeneration {

    /**
     * @return an instance of the repository factory as configured up to this point.
     * @see Start for configuration options.
     */
    RepositoryFactory build();

    /**
     * @return the {@link RepositoryFactoryConfiguration repository factory configuration} instance that has
     * been created as a result of method calls via this DSL
     */
    RepositoryFactoryConfiguration configure();

}
