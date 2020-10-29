package com.mmnaseri.utils.spring.data.tools;

import java.util.ArrayDeque;
import java.util.ArrayList;
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

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public final class CollectionInstanceUtils {

  private CollectionInstanceUtils() {
    throw new UnsupportedOperationException();
  }

  /**
   * Given any of the known collection types, this method will return an instance of the collection.
   *
   * @param collectionType the type of the collection
   * @return the collection instance
   */
  public static Collection<?> getCollection(Class<?> collectionType) {
    if (HashSet.class.equals(collectionType)) {
      return new HashSet<>();
    } else if (TreeSet.class.equals(collectionType)) {
      return new TreeSet<>();
    } else if (CopyOnWriteArraySet.class.equals(collectionType)) {
      return new CopyOnWriteArraySet<>();
    } else if (LinkedHashSet.class.equals(collectionType)) {
      return new LinkedHashSet<>();
    } else if (ArrayList.class.equals(collectionType)) {
      return new ArrayList<>();
    } else if (LinkedList.class.equals(collectionType)) {
      return new LinkedList<>();
    } else if (Vector.class.equals(collectionType)) {
      return new Vector<>();
    } else if (Stack.class.equals(collectionType)) {
      return new Stack<>();
    } else if (PriorityQueue.class.equals(collectionType)) {
      return new PriorityQueue<>();
    } else if (PriorityBlockingQueue.class.equals(collectionType)) {
      return new PriorityBlockingQueue<>();
    } else if (ArrayDeque.class.equals(collectionType)) {
      return new ArrayDeque<>();
    } else if (ConcurrentLinkedQueue.class.equals(collectionType)) {
      return new ConcurrentLinkedQueue<>();
    } else if (LinkedBlockingQueue.class.equals(collectionType)) {
      return new LinkedBlockingQueue<>();
    } else if (LinkedBlockingDeque.class.equals(collectionType)) {
      return new LinkedBlockingDeque<>();
    } else if (List.class.equals(collectionType)) {
      return new LinkedList<>();
    } else if (Set.class.equals(collectionType)) {
      return new HashSet<>();
    } else if (Queue.class.equals(collectionType)) {
      return new PriorityQueue<>();
    } else if (Deque.class.equals(collectionType)) {
      return new ArrayDeque<>();
    } else if (Collection.class.equals(collectionType)) {
      return new LinkedList<>();
    }
    throw new IllegalArgumentException("Unsupported collection type: " + collectionType);
  }
}
