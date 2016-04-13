package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface MappingContext extends OperationHandlers {

    EventListener withMappings(TypeMappingContext context);

    MappingContextAnd honoringImplementation(Class<?> superType, Class<?> implementation);

}
