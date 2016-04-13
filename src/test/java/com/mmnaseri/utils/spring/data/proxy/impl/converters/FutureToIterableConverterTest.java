package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.ResultConversionFailureException;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.Iterator;
import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class FutureToIterableConverterTest {

    @Test
    public void testConvertingFutureValueToIterable() throws Exception {
        final FutureToIterableConverter converter = new FutureToIterableConverter();
        final Object original = new Object();
        //noinspection unchecked
        final FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                return original;
            }
        });
        task.run();
        final Object converted = converter.convert(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findLong"), null), task);
        assertThat(converted, is(notNullValue()));
        assertThat(converted, is(instanceOf(Iterable.class)));
        final Iterable iterable = (Iterable) converted;
        final Iterator iterator = iterable.iterator();
        assertThat(iterator.hasNext(), is(true));
        final Object value = iterator.next();
        assertThat(value, is(notNullValue()));
        assertThat(value, is(original));
        assertThat(iterator.hasNext(), is(false));
    }

    @Test(expectedExceptions = ResultConversionFailureException.class)
    public void testConvertingFutureError() throws Exception {
        final FutureToIterableConverter converter = new FutureToIterableConverter();
        //noinspection unchecked
        final FutureTask task = new FutureTask(new Callable() {
            @Override
            public Object call() throws Exception {
                throw new RuntimeException();
            }
        });
        task.run();
        converter.convert(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findLong"), null), task);
    }

}