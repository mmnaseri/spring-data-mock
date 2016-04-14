package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public abstract class EntityDefinitionException extends RepositoryMockException {

    public EntityDefinitionException(String message) {
        super(message);
    }

}
