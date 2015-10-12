package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class SequentialIntegerKeyGenerator implements KeyGenerator<Integer> {

    private final AtomicInteger seed = new AtomicInteger(1);

    @Override
    public Integer generate() {
        return seed.getAndIncrement();
    }

}
