package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.impl.AbstractRandomKeyGenerator;

import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 7:02 PM)
 */
public class DuplicatingKeyGenerator extends AbstractRandomKeyGenerator<Integer> {

    private boolean repeat = false;
    private AtomicInteger counter = new AtomicInteger(0);

    @Override
    protected Integer getNext() {
        if (!repeat) {
            counter.incrementAndGet();
            repeat = true;
        } else {
            repeat = false;
        }
        return counter.get();
    }

}
