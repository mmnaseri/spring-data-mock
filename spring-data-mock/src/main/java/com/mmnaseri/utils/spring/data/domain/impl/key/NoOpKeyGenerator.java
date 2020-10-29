package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

/**
 * This is a key generator that should be used to indicate that we do not want automatic key generation.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/14/16, 12:51 PM)
 */
public class NoOpKeyGenerator<S> implements KeyGenerator<S> {

    @Override
    public S generate() {
        return null;
    }

}
