package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.domain.model.Address;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.proxy.impl.adapters.AbstractResultAdapter;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.concurrent.atomic.AtomicLong;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultResultAdapterContextTest {

    private interface Sample {

        Person findPerson();

    }

    private static class SpyingResultAdapter extends AbstractResultAdapter {

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

    @Test(expectedExceptions = ResultAdapterFailureException.class)
    public void testInvalidConversion() throws Exception {
        final DefaultResultAdapterContext context = new DefaultResultAdapterContext();
        context.adapt(new ImmutableInvocation(Sample.class.getMethod("findPerson"), new Object[]{}), new Address());
    }

    @Test
    public void testRegisteringOrderedAdapters() throws Exception {
        final DefaultResultAdapterContext context = new DefaultResultAdapterContext();
        final AtomicLong counter = new AtomicLong();
        final Person person = new Person();
        final SpyingResultAdapter first = new SpyingResultAdapter(-10000, counter, false, null);
        final SpyingResultAdapter second = new SpyingResultAdapter(10000, counter, true, person);
        context.register(first);
        context.register(second);
        final Object result = context.adapt(new ImmutableInvocation(Sample.class.getMethod("findPerson"), new Object[]{}), new Address());
        assertThat(first.getCheck(), is(notNullValue()));
        assertThat(second.getCheck(), is(notNullValue()));
        assertThat(first.getCheck(), is(lessThan(second.getCheck())));
        assertThat(first.getRequest(), is(nullValue()));
        assertThat(second.getCheck(), is(lessThan(second.getRequest())));
        assertThat(result, Matchers.<Object>is(person));
    }
}