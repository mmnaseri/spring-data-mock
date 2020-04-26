package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/12/16, 5:58 PM)
 */
public class PrimitiveIdTypeException extends EntityDefinitionException {

    public PrimitiveIdTypeException(Class<?> entityType, String idProperty) {
        super("The ID property (" + idProperty + ") found on entity <" + entityType
                      + "> is of a primitive type. Primitive types are not supported by this framework.");
    }

}
