package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.io.Serializable;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface KeyGeneration extends Implementation {

    <S extends Serializable> Implementation generateKeysUsing(KeyGenerator<S> keyGenerator);

    <S extends Serializable, G extends KeyGenerator<S>> Implementation generateKeysUsing(Class<G> generatorType);

    Implementation withoutGeneratingKeys();

}
