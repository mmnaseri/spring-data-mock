package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/5/15)
 */
public class NullSimpleResultAdapterTest {

    @Test
    public void testAcceptance() throws Exception {
        final NullSimpleResultAdapter adapter = new NullSimpleResultAdapter();
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findList"), new Object[]{}), null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findIterator"), new Object[]{}), null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findFuture"), new Object[]{}), null), is(false));
    }

    @Test
    public void testAdaptingNull() throws Exception {
        final NullSimpleResultAdapter adapter = new NullSimpleResultAdapter();
        final Object value = adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), null);
        assertThat(value, is(nullValue()));
    }

    @Test(expectedExceptions = ResultAdapterFailureException.class)
    public void testAdaptingPrimitiveToNull() throws Exception {
        final NullSimpleResultAdapter adapter = new NullSimpleResultAdapter();
        adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findPrimitive"), new Object[]{}), null);
    }

}