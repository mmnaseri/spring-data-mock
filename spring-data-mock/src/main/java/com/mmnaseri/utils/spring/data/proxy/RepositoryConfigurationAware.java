package com.mmnaseri.utils.spring.data.proxy;

/**
 * Used to passed the repository configuration to implementing classes.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public interface RepositoryConfigurationAware extends DependencyAware {

  /**
   * Called when the bound implementation class needs to know about the repository configuration.
   *
   * @param repositoryConfiguration the repository configuration
   */
  void setRepositoryConfiguration(RepositoryConfiguration repositoryConfiguration);
}
