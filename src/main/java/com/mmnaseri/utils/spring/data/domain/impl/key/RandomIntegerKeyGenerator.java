package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.impl.AbstractRandomKeyGenerator;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class RandomIntegerKeyGenerator extends AbstractRandomKeyGenerator<Integer> {
    
    private final Random random = ThreadLocalRandom.current();

    @Override
    protected Integer getNext() {
        return random.nextInt();
    }

}
