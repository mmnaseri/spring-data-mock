package com.mmnaseri.utils.spring.data.dsl.mock;

/**
 * Lets us create a mock of the repository we have in mind
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface End {

    /**
     * Tells the builder that it is now time to mock the given repository interface using the configuration provided
     * thus far.
     * @param repositoryInterface    the repository interface to mock
     * @param <E>                    the type of the repository
     * @return the mocked instance
     */
    <E> E mock(Class<E> repositoryInterface);

}
