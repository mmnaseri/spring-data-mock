package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class VoidResultAdapterTest {

    private interface Sample {

        void doSomething();

        Object findSomething();

    }

    @Test
    public void testAccepting() throws Exception {
        final VoidResultAdapter adapter = new VoidResultAdapter();
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findSomething"), new Object[]{}), null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("doSomething"), new Object[]{}), null), is(true));
    }

    @Test
    public void testAdapting() throws Exception {
        final VoidResultAdapter adapter = new VoidResultAdapter();
        final Object adapted = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("doSomething"), new Object[]{}), new Object());
        assertThat(adapted, is(nullValue()));
    }

}