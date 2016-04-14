package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 7:16 PM)
 */
public class CustomStringKeyGenerator implements KeyGenerator<String> {

    private final AtomicLong counter = new AtomicLong(0);

    @Override
    public String generate() {
        return String.valueOf(counter.incrementAndGet());
    }

}
