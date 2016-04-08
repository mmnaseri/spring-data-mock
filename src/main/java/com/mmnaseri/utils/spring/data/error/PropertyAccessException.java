package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class PropertyAccessException extends DataOperationException {

    public PropertyAccessException(Object entity, String propertyName, Throwable cause) {
        super("Failed to access " + propertyName + " of entity <" + entity + ">", cause);
    }

}
