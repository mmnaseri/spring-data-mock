package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class SequentialLongKeyGenerator implements KeyGenerator<Long> {

    private final AtomicLong seed = new AtomicLong(1);

    @Override
    public Long generate() {
        return seed.getAndIncrement();
    }

}
