package com.mmnaseri.utils.spring.data.domain;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public interface KeyGeneratorAware<S extends Serializable> {

    void setKeyGenerator(KeyGenerator<S> keyGenerator);

}
