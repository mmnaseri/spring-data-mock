package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Slice;
import org.springframework.data.geo.GeoPage;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class NullToSliceResultAdapterTest {

    private interface Sample {

        Slice findSlice();

        Page findPage();

        GeoPage findGeoPage();

    }

    @Test
    public void testAcceptance() throws Exception {
        final NullToSliceResultAdapter adapter = new NullToSliceResultAdapter();
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findSlice"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findPage"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findGeoPage"), new Object[]{}), null), is(true));
    }

    @Test
    public void testAdaptingToASlice() throws Exception {
        final NullToSliceResultAdapter adapter = new NullToSliceResultAdapter();
        final Slice<?> value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findSlice"), new Object[]{}), null);
        assertThat(value, is(notNullValue()));
        assertThat(value.getNumber(), is(0));
        assertThat(value.getNumberOfElements(), is(0));
        assertThat(value.getSize(), is(0));
        assertThat(value.getSort(), is(nullValue()));
        assertThat(value.getContent(), hasSize(0));
    }

    @Test
    public void testAdaptingToAPage() throws Exception {
        final NullToSliceResultAdapter adapter = new NullToSliceResultAdapter();
        final Slice<?> value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findPage"), new Object[]{}), null);
        assertThat(value, is(notNullValue()));
        assertThat(value.getNumber(), is(0));
        assertThat(value.getNumberOfElements(), is(0));
        assertThat(value.getSize(), is(0));
        assertThat(value.getSort(), is(nullValue()));
        assertThat(value.getContent(), hasSize(0));
    }

    @Test
    public void testAdaptingToAGeoPage() throws Exception {
        final NullToSliceResultAdapter adapter = new NullToSliceResultAdapter();
        final Slice<?> value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findGeoPage"), new Object[]{}), null);
        assertThat(value, is(notNullValue()));
        assertThat(value.getNumber(), is(0));
        assertThat(value.getNumberOfElements(), is(0));
        assertThat(value.getSize(), is(0));
        assertThat(value.getSort(), is(nullValue()));
        assertThat(value.getContent(), hasSize(0));
    }

}