package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.util.HashSet;
import java.util.Set;

/**
 * This implementation will wrap the key generation process in a procedure that prevents duplicate keys from being
 * generated. Since the {@link #generate() generate} method on this class is <em>synchronized</em> it protects
 * multi-threading and race issues from messing up the key generation, so the extending classes can easily generate
 * keys without having to worry about such issues.
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/8/15)
 */
public abstract class AbstractRandomKeyGenerator<S> implements KeyGenerator<S> {

    private final Set<S> used = new HashSet<>();

    @Override
    public final synchronized S generate() {
        S value;
        do {
            value = getNext();
        } while (used.contains(value));
        used.add(value);
        return value;
    }

    /**
     * @return the next key in the sequence. This needs not be reproducible.
     */
    protected abstract S getNext();

}
