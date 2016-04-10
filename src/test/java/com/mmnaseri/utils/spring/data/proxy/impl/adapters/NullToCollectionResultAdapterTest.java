package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import org.testng.annotations.Test;

import java.util.*;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class NullToCollectionResultAdapterTest {

    private interface Sample {

        List findList();

        Queue findQueue();

        Set findSet();

        LinkedList findLinkedList();

    }

    @Test
    public void testAcceptance() throws Exception {
        final NullToCollectionResultAdapter adapter = new NullToCollectionResultAdapter();
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findList"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findQueue"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findSet"), new Object[]{}), null), is(true));
        assertThat(adapter.accepts(new ImmutableInvocation(Sample.class.getMethod("findLinkedList"), new Object[]{}), null), is(true));
    }

    @Test
    public void testAdaptingList() throws Exception {
        final NullToCollectionResultAdapter adapter = new NullToCollectionResultAdapter();
        final Collection<?> collection = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findList"), new Object[]{}), null);
        assertThat(collection, is(notNullValue()));
        assertThat(collection, hasSize(0));
        assertThat(collection, is(instanceOf(List.class)));
    }

    @Test
    public void testAdaptingSet() throws Exception {
        final NullToCollectionResultAdapter adapter = new NullToCollectionResultAdapter();
        final Collection<?> collection = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findSet"), new Object[]{}), null);
        assertThat(collection, is(notNullValue()));
        assertThat(collection, hasSize(0));
        assertThat(collection, is(instanceOf(Set.class)));
    }

    @Test
    public void testAdaptingQueue() throws Exception {
        final NullToCollectionResultAdapter adapter = new NullToCollectionResultAdapter();
        final Collection<?> collection = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findQueue"), new Object[]{}), null);
        assertThat(collection, is(notNullValue()));
        assertThat(collection, hasSize(0));
        assertThat(collection, is(instanceOf(Queue.class)));
    }

    @Test
    public void testAdaptingConcreteCollection() throws Exception {
        final NullToCollectionResultAdapter adapter = new NullToCollectionResultAdapter();
        final Collection<?> collection = adapter.adapt(new ImmutableInvocation(Sample.class.getMethod("findLinkedList"), new Object[]{}), null);
        assertThat(collection, is(notNullValue()));
        assertThat(collection, hasSize(0));
        assertThat(collection, is(instanceOf(LinkedList.class)));
    }

}