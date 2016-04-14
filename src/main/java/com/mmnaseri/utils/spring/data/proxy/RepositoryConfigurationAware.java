package com.mmnaseri.utils.spring.data.proxy;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public interface RepositoryConfigurationAware extends DependencyAware {

    void setRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration);

}
