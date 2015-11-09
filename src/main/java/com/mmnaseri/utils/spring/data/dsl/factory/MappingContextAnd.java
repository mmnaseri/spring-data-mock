package com.mmnaseri.utils.spring.data.dsl.factory;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface MappingContextAnd extends EventListener {

    MappingContextAnd and(Class<?> superType, Class<?> implementation);

}
