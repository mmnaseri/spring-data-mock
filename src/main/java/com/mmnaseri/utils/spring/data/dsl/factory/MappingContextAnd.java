package com.mmnaseri.utils.spring.data.dsl.factory;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface MappingContextAnd extends OperationHandlers {

    MappingContextAnd and(Class<?> superType, Class<?> implementation);

}
