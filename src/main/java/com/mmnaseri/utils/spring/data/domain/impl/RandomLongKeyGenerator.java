package com.mmnaseri.utils.spring.data.domain.impl;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class RandomLongKeyGenerator extends AbstractRandomKeyGenerator<Long> {

    private final Random seed = ThreadLocalRandom.current();

    @Override
    protected Long getNext() {
        return seed.nextLong();
    }

}
