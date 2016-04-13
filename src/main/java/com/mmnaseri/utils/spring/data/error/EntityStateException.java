package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/8/16)
 */
public abstract class EntityStateException extends RepositoryMockException {

    public EntityStateException(String message) {
        super(message);
    }

}
