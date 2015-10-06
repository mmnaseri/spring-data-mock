package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class CollectionResultAdapterTest {

    private interface Sample {

        Set findSet();

    }

    @Test
    public void testAdapting() throws Exception {
        final CollectionResultAdapter adapter = new CollectionResultAdapter();
        final Collection<?> value = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findSet"), null), Arrays.asList(1, 2, 3, 4, 5, 4, 3, 2, 1));
        assertThat(value, is(notNullValue()));
        assertThat(value, hasSize(5));
        assertThat(value, is(instanceOf(Set.class)));
        assertThat(value, containsInAnyOrder((Object) 1, 2, 3, 4, 5));
    }

}