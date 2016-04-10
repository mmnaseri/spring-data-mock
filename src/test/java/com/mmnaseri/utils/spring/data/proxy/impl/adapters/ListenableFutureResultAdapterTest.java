package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.hamcrest.Matchers;
import org.springframework.util.concurrent.ListenableFuture;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class ListenableFutureResultAdapterTest {

    private interface Sample {

        ListenableFuture findListenableFuture();

        Object findOther();

    }

    @Test
    public void testAccepting() throws Exception {
        final ListenableFutureResultAdapter adapter = new ListenableFutureResultAdapter();
        assertThat(adapter.accepts(null, null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findOther"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findListenableFuture"), new Object[]{}), new Object()), is(true));
    }

    @Test
    public void testAdapting() throws Exception {
        final ListenableFutureResultAdapter adapter = new ListenableFutureResultAdapter();
        final List<Integer> originalValue = Arrays.asList(1, 2, 3);
        final ListenableFuture adapted = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findListenableFuture"), new Object[]{}), originalValue);
        assertThat(adapted, is(notNullValue()));
        final Object result = adapted.get();
        assertThat(result, is(notNullValue()));
        assertThat(result, Matchers.<Object>is(originalValue));
    }

}