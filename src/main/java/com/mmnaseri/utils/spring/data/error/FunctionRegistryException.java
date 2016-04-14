package com.mmnaseri.utils.spring.data.error;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public abstract class FunctionRegistryException extends RepositoryMockException {

    public FunctionRegistryException(String message) {
        super(message);
    }

}
