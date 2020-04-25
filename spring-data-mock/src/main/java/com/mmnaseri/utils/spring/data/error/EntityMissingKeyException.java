package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class EntityMissingKeyException extends EntityStateException {

    public EntityMissingKeyException(Class<?> entityType, String keyProperty) {
        super("An object of instance " + entityType + " must declare a valid key under property " + keyProperty);
    }

}
