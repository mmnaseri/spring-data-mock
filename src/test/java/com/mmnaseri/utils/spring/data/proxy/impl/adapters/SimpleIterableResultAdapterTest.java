package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class SimpleIterableResultAdapterTest {

    private interface Sample {

        Object findObject();

        Integer findInteger();

        Iterator findIterator();

        Iterable findIterable();

        Future findFuture();

    }

    @Test
    public void testAdaptingEmptyCollection() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        final Object value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findObject"), null), Collections.emptyList());
        assertThat(value, is(nullValue()));
    }

    @Test
    public void testAdaptingSingletonList() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        final Object value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findObject"), null), Collections.singletonList(1));
        assertThat(value, is(notNullValue()));
        assertThat(value, is((Object) 1));
    }

    @Test(expectedExceptions = ResultAdapterFailureException.class, expectedExceptionsMessageRegExp = ".*?; Expected only one item but found many")
    public void testAdaptingMultiItemList() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findObject"), null), Arrays.asList(1, 2, 3));
    }

    @Test(expectedExceptions = ResultAdapterFailureException.class, expectedExceptionsMessageRegExp = "Could not adapt value: <hello> to type <class java.lang.Integer>; Expected value to be of the indicated type")
    public void testAdaptingIncompatibleValue() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findInteger"), null), Collections.singletonList("hello"));
    }

    @Test
    public void testAccepting() throws Exception {
        final SimpleIterableResultAdapter adapter = new SimpleIterableResultAdapter();
        assertThat(adapter.accepts(null, null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findFuture"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findIterable"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findIterator"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findObject"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findInteger"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findObject"), new Object[]{}), new ArrayList<>()), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findInteger"), new Object[]{}), new ArrayList<>()), is(true));
    }

}