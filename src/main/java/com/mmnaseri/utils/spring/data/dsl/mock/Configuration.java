package com.mmnaseri.utils.spring.data.dsl.mock;

import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface Configuration {

    KeyGeneration useConfiguration(RepositoryFactoryConfiguration configuration);

}
