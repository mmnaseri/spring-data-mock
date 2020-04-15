package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

/**
 * Lets us set up how we want our keys to be generated
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface KeyGeneration extends Implementation {

    /**
     * Sets the key generator to the provided instance
     * @param keyGenerator    the key generator
     * @param <S>             the type of the keys
     * @return the rest of the configuration
     */
    <S> Implementation generateKeysUsing(KeyGenerator<S> keyGenerator);

    /**
     * Sets the key generator to an instance of the provided type.
     * @param generatorType    the type of the key generator to use
     * @param <S>              the type of the keys the generator will be generating
     * @param <G>              the type of the generator
     * @return the rest of the configuration
     */
    <S, G extends KeyGenerator<S>> Implementation generateKeysUsing(Class<G> generatorType);

    /**
     * Tells the builder that we are not going to have any auto-generated keys
     *
     * @return the rest of the configuration
     */
    Implementation withoutGeneratingKeys();

}
