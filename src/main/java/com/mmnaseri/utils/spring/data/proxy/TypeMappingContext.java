package com.mmnaseri.utils.spring.data.proxy;

import java.util.List;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/8/15)
 */
public interface TypeMappingContext {

    void register(Class<?> repositoryType, Class<?> implementation);

    List<Class<?>> getImplementations(Class<?> repositoryType);

    List<TypeMapping<?>> getMappings(Class<?> repositoryType);

}
