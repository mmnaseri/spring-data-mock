package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class NullToIteratorResultAdapterTest {

    @Test
    public void testAcceptance() throws Exception {
        final ResultAdapter<Iterator> adapter = new NullToIteratorResultAdapter();
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findIterator"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), null), is(false));
    }

    @Test
    public void testAdaptingToIterable() throws Exception {
        final ResultAdapter<Iterator> adapter = new NullToIteratorResultAdapter();
        final Iterator value = adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findIterator"), new Object[]{}), null);
        assertThat(value, is(notNullValue()));
        assertThat(value.hasNext(), is(false));
    }

}