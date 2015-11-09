package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class IteratorToIterableConverterTest {

    private interface Sample {

        Long findSomething();

    }

    @Test
    public void testConvertingIteratorToIterable() throws Exception {
        final IteratorToIterableConverter converter = new IteratorToIterableConverter();
        final Object converted = converter.convert(new ImmutableInvocation(Sample.class.getMethod("findSomething"), null), Arrays.asList(1, 2, 3, 4).iterator());
        assertThat(converted, is(notNullValue()));
        assertThat(converted, is(instanceOf(Iterable.class)));
        final Iterable iterable = (Iterable) converted;
        final Iterator iterator = iterable.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), equalTo((Object) 1));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), equalTo((Object) 2));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), equalTo((Object) 3));
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), equalTo((Object) 4));
        assertThat(iterator.hasNext(), is(false));
    }

}