package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.impl.key.RandomLongKeyGenerator;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/8/15)
 */
public class RandomLongKeyGeneratorTest extends BaseKeyGeneratorTest<Long> {

    @Override
    protected KeyGenerator<Long> getKeyGenerator() {
        return new RandomLongKeyGenerator();
    }

}