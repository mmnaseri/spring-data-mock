package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.impl.key.SequentialLongKeyGenerator;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public class SequentialLongKeyGeneratorTest extends BaseKeyGeneratorTest<Long> {

    @Override
    protected KeyGenerator<Long> getKeyGenerator() {
        return new SequentialLongKeyGenerator();
    }

    @Test
    public void testKeysBeingSequential() {
        final KeyGenerator<Long> keyGenerator = getKeyGenerator();
        Long last = 0L;
        for (int i = 0; i < 100; i++) {
            final Long key = keyGenerator.generate();
            assertThat(key, is(last + 1));
            last = key;
        }
    }

}