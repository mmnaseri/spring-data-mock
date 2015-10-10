package com.mmnaseri.utils.spring.data.domain;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/9/15)
 */
public interface RepositoryAware<R> {

    void setRepository(R repository);

}
