package com.mmnaseri.utils.spring.data.proxy;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 1:34 PM)
 */
public interface RepositoryFactoryAware extends DependencyAware {

    void setRepositoryFactory(RepositoryFactory factory);

}
