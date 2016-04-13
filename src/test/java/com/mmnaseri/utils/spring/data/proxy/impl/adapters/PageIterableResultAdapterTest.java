package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class PageIterableResultAdapterTest {

    @Test
    public void testAdapting() throws Exception {
        final PageIterableResultAdapter adapter = new PageIterableResultAdapter();
        final org.springframework.data.domain.Page<?> value = adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findPage"), null), Arrays.asList(1, 2, 3, 4));
        assertThat(value, is(notNullValue()));
        assertThat(value.getTotalElements(), is(4L));
        assertThat(value.getTotalPages(), is(1));
        assertThat(value.getNumber(), is(0));
        assertThat(value.getNumberOfElements(), is(4));
        assertThat(value.getSize(), is(0));
        assertThat(value.getSort(), is(nullValue()));
        assertThat(value.getContent(), hasSize(4));
        assertThat(value.getContent(), containsInAnyOrder((Object) 1, 2, 3, 4));
    }

    @Test
    public void testAccepting() throws Exception {
        final PageIterableResultAdapter adapter = new PageIterableResultAdapter();
        assertThat(adapter.accepts(null, null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findPage"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findPage"), new Object[]{}), new ArrayList<>()), is(true));
    }

}