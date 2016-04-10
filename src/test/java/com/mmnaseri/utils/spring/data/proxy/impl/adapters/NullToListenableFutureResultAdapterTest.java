package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.springframework.util.concurrent.ListenableFuture;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class NullToListenableFutureResultAdapterTest {

    private interface Sample {

        ListenableFuture findListenableFuture();

        Object findOther();

    }

    @Test
    public void testAccepting() throws Exception {
        final NullToListenableFutureResultAdapter adapter = new NullToListenableFutureResultAdapter();
        assertThat(adapter.accepts(null, new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findOther"), new Object[]{}), null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findListenableFuture"), new Object[]{}), null), is(true));
    }

    @Test
    public void testAdaptingTheResult() throws Exception {
        final NullToListenableFutureResultAdapter adapter = new NullToListenableFutureResultAdapter();
        final ListenableFuture adapted = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findListenableFuture"), new Object[]{}), null);
        assertThat(adapted, is(notNullValue()));
        assertThat(adapted.get(), is(nullValue()));
    }

}