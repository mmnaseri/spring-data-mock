package com.mmnaseri.utils.spring.data.error;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public abstract class EntityDefinitionException extends RepositoryMockException {

    public EntityDefinitionException(String message) {
        super(message);
    }

    public EntityDefinitionException(String message, Throwable cause) {
        super(message, cause);
    }

}
