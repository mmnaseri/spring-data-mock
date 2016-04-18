package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.TypeMappingContext;

/**
 * Lets us configure the type mapping
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface MappingContext extends OperationHandlers {

    /**
     * Tells the builder to use the provided context
     * @param context    the context
     * @return the rest of the configuration
     */
    EventListener withMappings(TypeMappingContext context);

    /**
     * Tells the builder to register a mapping
     * @param superType         the super type for the repository interface
     * @param implementation    the concrete class implementing the mapped methods
     * @return the rest of the configuration
     */
    MappingContextAnd honoringImplementation(Class<?> superType, Class<?> implementation);

}
