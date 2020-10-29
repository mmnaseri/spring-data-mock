package com.mmnaseri.utils.spring.data.sample.repositories;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 7:09 PM)
 */
public interface ConfiguredSimplePersonRepository extends SimplePersonRepository {

    RepositoryConfiguration getRepositoryConfiguration();

}
