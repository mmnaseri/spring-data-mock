package com.mmnaseri.utils.spring.data.proxy;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;

import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/12/15)
 */
public interface RepositoryConfiguration {

    RepositoryMetadata getRepositoryMetadata();

    KeyGenerator<?> getKeyGenerator();

    List<Class<?>> getBoundImplementations();

}
