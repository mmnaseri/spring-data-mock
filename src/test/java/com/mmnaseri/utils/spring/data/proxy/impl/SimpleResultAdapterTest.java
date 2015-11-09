package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class SimpleResultAdapterTest {

    private interface Sample {

        Object findObject();

        Integer findInteger();

    }

    @Test
    public void testAdaptingEmptyCollection() throws Exception {
        final SimpleResultAdapter adapter = new SimpleResultAdapter();
        final Object value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findObject"), null), Collections.emptyList());
        assertThat(value, is(nullValue()));
    }

    @Test
    public void testAdaptingSingletonList() throws Exception {
        final SimpleResultAdapter adapter = new SimpleResultAdapter();
        final Object value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findObject"), null), Collections.singletonList(1));
        assertThat(value, is(notNullValue()));
        assertThat(value, is((Object) 1));
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Expected to get only one item but got many")
    public void testAdaptingMultiItemList() throws Exception {
        final SimpleResultAdapter adapter = new SimpleResultAdapter();
        adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findObject"), null), Arrays.asList(1, 2, 3));
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Expected a value of type class java.lang.Integer")
    public void testAdaptingIncompatibleValue() throws Exception {
        final SimpleResultAdapter adapter = new SimpleResultAdapter();
        adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findInteger"), null), Collections.singletonList("hello"));
    }

}