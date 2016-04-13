package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.springframework.util.concurrent.ListenableFuture;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class NullToListenableFutureResultAdapterTest {

    @Test
    public void testAccepting() throws Exception {
        final ResultAdapter<ListenableFuture> adapter = new NullToListenableFutureResultAdapter();
        assertThat(adapter.accepts(null, new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findListenableFuture"), new Object[]{}), null), is(true));
    }

    @Test
    public void testAdaptingTheResult() throws Exception {
        final ResultAdapter<ListenableFuture> adapter = new NullToListenableFutureResultAdapter();
        final ListenableFuture adapted = adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findListenableFuture"), new Object[]{}), null);
        assertThat(adapted, is(notNullValue()));
        assertThat(adapted.get(), is(nullValue()));
    }

}