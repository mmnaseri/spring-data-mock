package com.mmnaseri.utils.spring.data.proxy;

/**
 * This interface is used when an implementing class needs to know about the repository factory configuration.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/9/15)
 */
public interface RepositoryFactoryConfigurationAware extends DependencyAware {

    /**
     * This method is called to inject the repository factory configuration
     *
     * @param configuration the configuration
     */
    void setRepositoryFactoryConfiguration(RepositoryFactoryConfiguration configuration);

}
