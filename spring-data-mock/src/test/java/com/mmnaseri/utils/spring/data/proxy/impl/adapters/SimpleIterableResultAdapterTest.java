package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class SimpleIterableResultAdapterTest {

    @Test
    public void testAdaptingEmptyCollection() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        final Object value = adapter.adapt(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), null),
                Collections.emptyList());
        assertThat(value, is(nullValue()));
    }

    @Test
    public void testAdaptingSingletonList() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        final Object value = adapter.adapt(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), null),
                Collections.singletonList(1));
        assertThat(value, is(notNullValue()));
        assertThat(value, is((Object) 1));
    }

    @Test(expectedExceptions = ResultAdapterFailureException.class,
          expectedExceptionsMessageRegExp = ".*?; Expected only one item but found many")
    public void testAdaptingMultiItemList() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), null),
                      Arrays.asList(1, 2, 3));
    }

    @Test(expectedExceptions = ResultAdapterFailureException.class,
          expectedExceptionsMessageRegExp = "Could not adapt value: <hello> to type <class java.lang.Integer>; " 
                  + "Expected value to be of the indicated type")
    public void testAdaptingIncompatibleValue() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findInteger"), null),
                      Collections.singletonList("hello"));
    }

    @Test
    public void testAccepting() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        assertThat(adapter.accepts(null, null), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findFuture"), new Object[]{}),
                new Object()), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findIterable"), new Object[]{}),
                new Object()), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findIterator"), new Object[]{}),
                new Object()), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}),
                new Object()), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findInteger"), new Object[]{}),
                new Object()), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}),
                new ArrayList<>()), is(true));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findInteger"), new Object[]{}),
                new ArrayList<>()), is(true));
    }

}