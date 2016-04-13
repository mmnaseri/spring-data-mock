package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.io.Serializable;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface RepositoryFactory {

    <E> E getInstance(KeyGenerator<? extends Serializable> keyGenerator, Class<E> repositoryInterface, Class... implementations);

    RepositoryFactoryConfiguration getConfiguration();

}
