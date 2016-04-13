package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.ResultAdapterFailureException;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class CollectionIterableResultAdapterTest {

    @Test
    public void testAdapting() throws Exception {
        final CollectionIterableResultAdapter adapter = new CollectionIterableResultAdapter();
        final Collection<?> value = adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findSet"), null), Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1));
        assertThat(value, is(notNullValue()));
        assertThat(value, hasSize(5));
        assertThat(value, is(instanceOf(Set.class)));
        assertThat(value, containsInAnyOrder((Object) 1, 2, 3, 4, 5));
    }

    @Test(expectedExceptions = ResultAdapterFailureException.class)
    public void testAdaptingCustomCollectionImplementation() throws Exception {
        final CollectionIterableResultAdapter adapter = new CollectionIterableResultAdapter();
        adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findCustomCollection"), null), null);
    }

    @Test
    public void testAccepts() throws Exception {
        final CollectionIterableResultAdapter adapter = new CollectionIterableResultAdapter();
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findCustomCollection"), null), null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findCustomCollection"), null), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findCustomCollection"), null), new ArrayList<>()), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findSet"), null), new ArrayList<>()), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), null), new ArrayList<>()), is(false));
    }

}