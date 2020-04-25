package com.mmnaseri.utils.spring.data.proxy;

/**
 * Indicates that the implementing class needs to know about the repository factory.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/12/16, 1:34 PM)
 */
public interface RepositoryFactoryAware extends DependencyAware {

    /**
     * Used to inject the repository factory into the implementing class
     *
     * @param factory the factory
     */
    void setRepositoryFactory(RepositoryFactory factory);

}
