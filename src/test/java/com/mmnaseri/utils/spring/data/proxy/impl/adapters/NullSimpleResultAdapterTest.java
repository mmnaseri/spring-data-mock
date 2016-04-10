package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class NullSimpleResultAdapterTest {

    private interface Sample {

        Object findSomething();

        List findAll();

        Future findInTheFuture();

        Iterator findByIterator();

        int findPrimitive();

    }

    @Test
    public void testAcceptance() throws Exception {
        final NullSimpleResultAdapter adapter = new NullSimpleResultAdapter();
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findSomething"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findSomething"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findAll"), new Object[]{}), null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findByIterator"), new Object[]{}), null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findInTheFuture"), new Object[]{}), null), is(false));
    }

    @Test
    public void testAdaptingNull() throws Exception {
        final NullSimpleResultAdapter adapter = new NullSimpleResultAdapter();
        final Object value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findSomething"), new Object[]{}), null);
        assertThat(value, is(nullValue()));
    }

    @Test(expectedExceptions = ResultAdapterFailureException.class)
    public void testAdaptingPrimitiveToNull() throws Exception {
        final NullSimpleResultAdapter adapter = new NullSimpleResultAdapter();
        adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findPrimitive"), new Object[]{}), null);
    }

}