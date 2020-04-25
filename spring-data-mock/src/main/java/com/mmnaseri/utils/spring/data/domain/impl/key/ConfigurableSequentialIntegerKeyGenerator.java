package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.impl.AbstractRandomKeyGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/14/16, 11:49 AM)
 */
public class ConfigurableSequentialIntegerKeyGenerator extends AbstractRandomKeyGenerator<Integer> {

    private final AtomicInteger current;
    private final int step;

    public ConfigurableSequentialIntegerKeyGenerator() {
        this(1);
    }

    public ConfigurableSequentialIntegerKeyGenerator(int initialValue) {
        this(initialValue, 1);
    }

    public ConfigurableSequentialIntegerKeyGenerator(int initialValue, int step) {
        current = new AtomicInteger(initialValue);
        this.step = step;
    }

    @Override
    protected Integer getNext() {
        return current.getAndAdd(step);
    }

}
