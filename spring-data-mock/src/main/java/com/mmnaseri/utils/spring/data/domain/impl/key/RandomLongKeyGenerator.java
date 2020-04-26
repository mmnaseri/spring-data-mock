package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.impl.AbstractRandomKeyGenerator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * This class will help with the generation of unique, random long numbrs.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class RandomLongKeyGenerator extends AbstractRandomKeyGenerator<Long> {

    private final Random seed = ThreadLocalRandom.current();

    @Override
    protected Long getNext() {
        return seed.nextLong();
    }

}
