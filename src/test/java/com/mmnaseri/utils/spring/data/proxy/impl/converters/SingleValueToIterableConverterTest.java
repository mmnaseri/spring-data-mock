package com.mmnaseri.utils.spring.data.proxy.impl.converters;

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
public class SingleValueToIterableConverterTest {

    private interface Sample {

        Long findSomething();

    }

    @Test
    public void testConvertingSingleValueToIterable() throws Exception {
        final SingleValueToIterableConverter converter = new SingleValueToIterableConverter();
        final Object original = new Object();
        final Object converted = converter.convert(new ImmutableInvocation(Sample.class.getMethod("findSomething"), null), original);
        assertThat(converted, is(notNullValue()));
        assertThat(converted, is(instanceOf(Iterable.class)));
        final Iterable<?> iterable = (Iterable<?>) converted;
        final Iterator<?> iterator = iterable.iterator();
        assertThat(iterator.hasNext(), is(true));
        assertThat(iterator.next(), is(original));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test
    public void testConvertingIterableToIterable() throws Exception {
        final SingleValueToIterableConverter converter = new SingleValueToIterableConverter();
        final Object original = Arrays.asList(1, 2, 3);
        final Object converted = converter.convert(new ImmutableInvocation(Sample.class.getMethod("findSomething"), null), original);
        assertThat(converted, is(original));
    }

    @Test
    public void testConvertingIteratorToIterable() throws Exception {
        final SingleValueToIterableConverter converter = new SingleValueToIterableConverter();
        final Object original = Arrays.asList(1, 2, 3).iterator();
        final Object converted = converter.convert(new ImmutableInvocation(Sample.class.getMethod("findSomething"), null), original);
        assertThat(converted, is(original));
    }

}