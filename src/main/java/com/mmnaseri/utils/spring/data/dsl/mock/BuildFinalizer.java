package com.mmnaseri.utils.spring.data.dsl.mock;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/9/15)
 */
public interface BuildFinalizer {

    <E> E instantiate(Class<E> repositoryInterface);

}
