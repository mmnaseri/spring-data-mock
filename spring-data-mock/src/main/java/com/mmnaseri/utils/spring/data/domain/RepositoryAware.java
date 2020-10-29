package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface is used to inject the repository itself into a concrete class aiming to provide
 * method mapping for a repository.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/9/15)
 */
public interface RepositoryAware<R> {

  void setRepository(R repository);
}
