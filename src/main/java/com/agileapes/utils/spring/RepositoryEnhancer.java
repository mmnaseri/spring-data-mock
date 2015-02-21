package com.agileapes.utils.spring;

import org.springframework.data.repository.Repository;

import java.io.Serializable;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/21 AD, 16:05)
 */
public interface RepositoryEnhancer {

    <E, K extends Serializable, R extends Repository<E, K>> R instantiate(RepositoryDescriptor<E, K, R> descriptor);

}
