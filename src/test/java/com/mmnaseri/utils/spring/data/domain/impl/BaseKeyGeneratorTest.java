package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import org.testng.annotations.Test;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public abstract class BaseKeyGeneratorTest<S extends Serializable> {

    protected abstract KeyGenerator<S> getKeyGenerator();

    @Test(invocationCount = 100)
    public void testThatKeysAreUnique() throws Exception {
        final KeyGenerator<S> keyGenerator = getKeyGenerator();
        final Set<S> keys = new HashSet<>();
        for (int i = 0; i < 200; i++) {
            final S key = keyGenerator.generate();
            assertThat(keys.contains(key), is(false));
            keys.add(key);
        }
    }

}
