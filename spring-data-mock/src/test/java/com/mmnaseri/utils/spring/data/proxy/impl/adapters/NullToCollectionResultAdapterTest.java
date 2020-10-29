package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class NullToCollectionResultAdapterTest {

  @Test
  public void testAcceptance() throws Exception {
    final ResultAdapter<Collection> adapter = new NullToCollectionResultAdapter();
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findList"), new Object[] {}),
            null),
        is(true));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findQueue"), new Object[] {}),
            null),
        is(true));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findSet"), new Object[] {}),
            null),
        is(true));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findLinkedList"), new Object[] {}),
            null),
        is(true));
  }

  @Test
  public void testAdaptingList() throws Exception {
    final ResultAdapter<Collection> adapter = new NullToCollectionResultAdapter();
    final Collection<?> collection =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findList"), new Object[] {}),
            null);
    assertThat(collection, is(notNullValue()));
    assertThat(collection, hasSize(0));
    assertThat(collection, is(instanceOf(List.class)));
  }

  @Test
  public void testAdaptingSet() throws Exception {
    final ResultAdapter<Collection> adapter = new NullToCollectionResultAdapter();
    final Collection<?> collection =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findSet"), new Object[] {}),
            null);
    assertThat(collection, is(notNullValue()));
    assertThat(collection, hasSize(0));
    assertThat(collection, is(instanceOf(Set.class)));
  }

  @Test
  public void testAdaptingQueue() throws Exception {
    final ResultAdapter<Collection> adapter = new NullToCollectionResultAdapter();
    final Collection<?> collection =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findQueue"), new Object[] {}),
            null);
    assertThat(collection, is(notNullValue()));
    assertThat(collection, hasSize(0));
    assertThat(collection, is(instanceOf(Queue.class)));
  }

  @Test
  public void testAdaptingConcreteCollection() throws Exception {
    final ResultAdapter<Collection> adapter = new NullToCollectionResultAdapter();
    final Collection<?> collection =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findLinkedList"), new Object[] {}),
            null);
    assertThat(collection, is(notNullValue()));
    assertThat(collection, hasSize(0));
    assertThat(collection, is(instanceOf(LinkedList.class)));
  }
}
