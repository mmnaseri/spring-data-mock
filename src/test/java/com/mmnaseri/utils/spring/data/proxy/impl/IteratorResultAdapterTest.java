package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class IteratorResultAdapterTest {

    private interface Sample {

        Iterator findIterator();

    }

    @Test
    public void testAdapting() throws Exception {
        final IteratorResultAdapter adapter = new IteratorResultAdapter();
        final Iterator<?> value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findIterator"), null), Arrays.asList(1, 2, 3, 4));
        assertThat(value, is(notNullValue()));
        int count = 0;
        while (value.hasNext()) {
            final Object item = value.next();
            assertThat(item, isIn(new Object[]{1, 2, 3, 4}));
            count ++;
        }
        assertThat(count, is(4));
    }

}