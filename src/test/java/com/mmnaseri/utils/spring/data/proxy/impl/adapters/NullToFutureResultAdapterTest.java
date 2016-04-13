package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class NullToFutureResultAdapterTest {

    @Test
    public void testAcceptance() throws Exception {
        final ResultAdapter<Future> adapter = new NullToFutureResultAdapter();
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findFuture"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), null), is(false));
    }

    @Test
    public void testAdaptingFuture() throws Exception {
        final ResultAdapter<Future> adapter = new NullToFutureResultAdapter();
        final Future value = adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findFuture"), new Object[]{}), null);
        assertThat(value, is(notNullValue()));
        assertThat(value.get(), is(nullValue()));
    }

}