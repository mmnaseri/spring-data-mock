package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public class RandomLongKeyGeneratorTest extends BaseKeyGeneratorTest<Long> {

    @Override
    protected KeyGenerator<Long> getKeyGenerator() {
        return new RandomLongKeyGenerator();
    }

}