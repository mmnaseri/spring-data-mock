package com.mmnaseri.utils.spring.data.sample.mocks;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.proxy.impl.adapters.AbstractResultAdapter;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/12/16, 6:48 PM)
 */
public class SpyingResultAdapter extends AbstractResultAdapter {

    private final AtomicLong counter;
    private Long check;
    private Long request;
    private final boolean accepts;
    private final Object result;

    public SpyingResultAdapter(int priority, AtomicLong counter, boolean accepts, Object result) {
        super(priority);
        this.counter = counter;
        this.accepts = accepts;
        this.result = result;
    }

    @Override
    public boolean accepts(Invocation invocation, Object originalValue) {
        check = counter.incrementAndGet();
        return accepts;
    }

    @Override
    public Object adapt(Invocation invocation, Object originalValue) {
        request = counter.incrementAndGet();
        return result;
    }

    public Long getCheck() {
        return check;
    }

    public Long getRequest() {
        return request;
    }

}
