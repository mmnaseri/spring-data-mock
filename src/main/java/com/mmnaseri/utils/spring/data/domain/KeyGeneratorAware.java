package com.mmnaseri.utils.spring.data.domain;

import java.io.Serializable;

/**
 * This interface is used to inject {@link KeyGenerator the key generator} into a concrete class aiming to provide method mapping
 * for a repository.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/8/15)
 */
public interface KeyGeneratorAware<S extends Serializable> {

    void setKeyGenerator(KeyGenerator<? extends S> keyGenerator);

}
