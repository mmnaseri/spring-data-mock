package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.springframework.data.domain.Slice;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class SliceIterableResultAdapterTest {

    @Test
    public void testAccepting() throws Exception {
        final SliceIterableResultAdapter adapter = new SliceIterableResultAdapter();
        assertThat(adapter.accepts(null, null), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}),
                new Object()), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findSlice"), new Object[]{}),
                new Object()), is(false));
        assertThat(adapter.accepts(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findSlice"), new Object[]{}),
                new ArrayList<>()), is(true));
    }

    @Test
    public void testAdapting() throws Exception {
        final SliceIterableResultAdapter adapter = new SliceIterableResultAdapter();
        final List<Object> originalValue = Arrays.asList(1, 2, 3, 4);
        final Slice slice = adapter.adapt(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findSlice"), new Object[]{}),
                originalValue);
        assertThat(slice, is(notNullValue()));
        assertThat(slice.getNumberOfElements(), is(originalValue.size()));
        final List content = slice.getContent();
        for (Object item : content) {
            assertThat(item, isIn(originalValue));
        }
    }
}