package com.mmnaseri.utils.spring.data.proxy;

import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public interface TypeMappingContext {

    void register(Class<?> repositoryType, Class<?> implementation);

    List<Class<?>> getImplementations(Class<?> repositoryType);

    List<TypeMapping<?>> getMappings(Class<?> repositoryType);

}
