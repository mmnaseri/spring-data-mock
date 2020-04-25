package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

/**
 * <p>This interface encapsulates the process of instantiating a repository. A repository factory is needed to
 * properly set up a repository instance.</p>
 *
 * <p>A default implementation is provided via {@link com.mmnaseri.utils.spring.data.proxy.impl.DefaultRepositoryFactory
 * default
 * repositor factory}.</p>
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface RepositoryFactory {

    /**
     * Creates an instance of the repository as per the provided configuration.
     *
     * @param keyGenerator        the key generator to use when inserting items (if auto generation is required). You
     *                            can specify a {@literal null} key generator to signify that {@link
     *                            RepositoryFactoryConfiguration#getDefaultKeyGenerator() the fallback key generator}
     *                            should be used when generating keys.
     * @param repositoryInterface the repository interface which we want to mock
     * @param implementations     all the concrete classes that can be used to figure out method mappings
     * @param <E>                 the type of the interface
     * @return a prepared instance of the repository
     * @throws com.mmnaseri.utils.spring.data.error.RepositoryMockException should anything go wrong
     */
    <E> E getInstance(KeyGenerator<?> keyGenerator, Class<E> repositoryInterface, Class... implementations);

    /**
     * @return the configuration bound to this repository factory
     */
    RepositoryFactoryConfiguration getConfiguration();

}
