package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.dsl.mock.KeyGeneration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactory;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface End extends KeyGeneration {

    RepositoryFactory build();

}
