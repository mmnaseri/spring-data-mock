package com.mmnaseri.utils.spring.data.sample.usecases.proxy;

import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.proxy.RepositoryFactoryConfiguration;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 11:13 AM)
 */
public class DefaultInformationExposingRepository implements InformationExposingRepository {

    private final RepositoryConfiguration configuration;
    private final RepositoryFactoryConfiguration factoryConfiguration;

    public DefaultInformationExposingRepository(RepositoryConfiguration configuration, RepositoryFactoryConfiguration factoryConfiguration) {
        this.configuration = configuration;
        this.factoryConfiguration = factoryConfiguration;
    }

    @Override
    public RepositoryFactoryConfiguration getFactoryConfiguration() {
        return factoryConfiguration;
    }

    @Override
    public RepositoryConfiguration getConfiguration() {
        return configuration;
    }

}
