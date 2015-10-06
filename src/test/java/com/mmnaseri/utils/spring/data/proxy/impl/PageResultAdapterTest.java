package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.query.Page;
import org.testng.annotations.Test;

import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class PageResultAdapterTest {

    private interface Sample {

        Page findPage();

    }

    @Test
    public void testAdapting() throws Exception {
        final PageResultAdapter adapter = new PageResultAdapter();
        final org.springframework.data.domain.Page<?> value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findPage"), null), Arrays.asList(1, 2, 3, 4));
        assertThat(value, is(notNullValue()));
        assertThat(value.getTotalElements(), is(4l));
        assertThat(value.getTotalPages(), is(1));
        assertThat(value.getNumber(), is(0));
        assertThat(value.getNumberOfElements(), is(4));
        assertThat(value.getSize(), is(0));
        assertThat(value.getSort(), is(nullValue()));
        assertThat(value.getContent(), hasSize(4));
        assertThat(value.getContent(), containsInAnyOrder((Object) 1, 2, 3, 4));
    }

}