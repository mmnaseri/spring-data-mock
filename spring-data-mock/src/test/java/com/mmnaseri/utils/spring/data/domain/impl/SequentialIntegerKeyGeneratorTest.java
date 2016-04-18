package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.impl.key.SequentialIntegerKeyGenerator;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/8/15)
 */
public class SequentialIntegerKeyGeneratorTest extends BaseKeyGeneratorTest<Integer> {

    @Override
    protected KeyGenerator<Integer> getKeyGenerator() {
        return new SequentialIntegerKeyGenerator();
    }

    @Test
    public void testKeysBeingSequential() throws Exception {
        final KeyGenerator<Integer> keyGenerator = getKeyGenerator();
        Integer last = 0;
        for (int i = 0; i < 100; i++) {
            final Integer key = keyGenerator.generate();
            assertThat(key, is(last + 1));
            last = key;
        }
    }

}