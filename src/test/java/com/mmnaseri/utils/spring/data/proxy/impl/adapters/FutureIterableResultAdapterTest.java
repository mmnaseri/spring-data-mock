package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.concurrent.Future;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/5/15)
 */
public class FutureIterableResultAdapterTest {

    @Test
    public void testAdapting() throws Exception {
        final FutureIterableResultAdapter adapter = new FutureIterableResultAdapter();
        final Future<?> value = adapter.adapt(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findFuture"), null), Arrays.asList(1, 2, 3, 4));
        assertThat(value, is(notNullValue()));
        assertThat(value.get(), is(instanceOf((Class) Collection.class)));
        final Collection<?> collection = (Collection<?>) value.get();
        assertThat(collection, hasSize(4));
        assertThat(collection, containsInAnyOrder((Object) 1, 2, 3, 4));
    }

    @Test
    public void testAccepting() throws Exception {
        final FutureIterableResultAdapter adapter = new FutureIterableResultAdapter();
        assertThat(adapter.accepts(null, null), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findFuture"), new Object[]{}), new Object()), is(false));
        assertThat(adapter.accepts(new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findFuture"), new Object[]{}), new ArrayList<>()), is(true));
    }

}