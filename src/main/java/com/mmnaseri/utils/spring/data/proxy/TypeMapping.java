package com.mmnaseri.utils.spring.data.proxy;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface TypeMapping<E> {

    Class<E> getType();

    E getInstance();

}
