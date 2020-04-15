package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface encapsulates the process of keys being generated when we need a solid key generation scheme to be in place
 * prior to entities being written to the data store.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/6/15)
 */
public interface KeyGenerator<S> {

    /**
     * Generates a new key and returns the value
     * @return the generated key
     */
    S generate();

}
