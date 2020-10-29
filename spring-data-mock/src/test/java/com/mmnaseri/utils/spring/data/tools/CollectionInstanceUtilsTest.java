package com.mmnaseri.utils.spring.data.tools;

import org.testng.annotations.Test;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.PriorityQueue;
import java.util.Queue;
import java.util.Set;
import java.util.Stack;
import java.util.TreeSet;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.LinkedBlockingDeque;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/8/16)
 */
public class CollectionInstanceUtilsTest extends AbstractUtilityClassTest {

  @Override
  protected Class<?> getUtilityClass() {
    return CollectionInstanceUtils.class;
  }

  @Test
  public void testSupportedConcreteTypes() {
    final List<? extends Class<?>> collectionTypes =
        Arrays.asList(
            HashSet.class,
            TreeSet.class,
            CopyOnWriteArraySet.class,
            LinkedHashSet.class,
            ArrayList.class,
            LinkedList.class,
            Vector.class,
            Stack.class,
            PriorityQueue.class,
            PriorityBlockingQueue.class,
            ArrayDeque.class,
            ConcurrentLinkedQueue.class,
            LinkedBlockingQueue.class,
            LinkedBlockingDeque.class);
    for (Class<?> collectionType : collectionTypes) {
      final Collection<?> collection = CollectionInstanceUtils.getCollection(collectionType);
      assertThat(collection, is(notNullValue()));
      assertThat(collection, is(instanceOf(collectionType)));
    }
  }

  @Test
  public void testSupportedAbstractTypes() {
    assertThat(CollectionInstanceUtils.getCollection(Set.class), is(instanceOf(Set.class)));
    assertThat(CollectionInstanceUtils.getCollection(List.class), is(instanceOf(List.class)));
    assertThat(CollectionInstanceUtils.getCollection(Queue.class), is(instanceOf(Queue.class)));
    assertThat(CollectionInstanceUtils.getCollection(Deque.class), is(instanceOf(Deque.class)));
    assertThat(
        CollectionInstanceUtils.getCollection(Collection.class), is(instanceOf(Collection.class)));
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testUnknownType() {
    CollectionInstanceUtils.getCollection(Class.class);
  }

  @Test(expectedExceptions = IllegalArgumentException.class)
  public void testNullValue() {
    CollectionInstanceUtils.getCollection(null);
  }
}
