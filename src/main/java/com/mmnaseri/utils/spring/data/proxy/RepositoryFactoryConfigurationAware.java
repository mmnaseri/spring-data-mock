package com.mmnaseri.utils.spring.data.proxy;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/9/15)
 */
public interface RepositoryFactoryConfigurationAware extends DependencyAware {

    void setRepositoryFactoryConfiguration(RepositoryFactoryConfiguration configuration);

}
