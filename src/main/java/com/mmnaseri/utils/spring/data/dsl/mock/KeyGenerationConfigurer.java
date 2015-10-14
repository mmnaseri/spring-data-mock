package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/9/15)
 */
public interface KeyGenerationConfigurer extends BuildFinalizer {

    <S extends Serializable> ImplementationConfigurer generateKeysUsing(KeyGenerator<S> generator);

    <S extends Serializable, G extends KeyGenerator<S>> ImplementationConfigurer generateKeysUsing(Class<G> generatorType);

}
