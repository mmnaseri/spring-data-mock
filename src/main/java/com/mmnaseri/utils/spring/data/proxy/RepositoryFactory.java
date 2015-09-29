package com.mmnaseri.utils.spring.data.proxy;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface RepositoryFactory {

    <E> E getInstance(Class<E> repositoryInterface, Class... implementations);

}
