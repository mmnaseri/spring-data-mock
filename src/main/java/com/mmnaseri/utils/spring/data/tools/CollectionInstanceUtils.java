package com.mmnaseri.utils.spring.data.tools;

import java.util.*;
import java.util.concurrent.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/24/15)
 */
public abstract class CollectionInstanceUtils {

    public static Collection<?> getCollection(Class<?> collectionType) {
        if (collectionType.equals(HashSet.class)) {
            return new HashSet<Object>();
        } else if (collectionType.equals(TreeSet.class)) {
            return new TreeSet<Object>();
        } else if (collectionType.equals(CopyOnWriteArraySet.class)) {
            return new CopyOnWriteArraySet<Object>();
        } else if (collectionType.equals(LinkedHashSet.class)) {
            return new LinkedHashSet<Object>();
        } else if (collectionType.equals(ArrayList.class)) {
            return new ArrayList<Object>();
        } else if (collectionType.equals(LinkedList.class)) {
            return new LinkedList<Object>();
        } else if (collectionType.equals(Vector.class)) {
            return new Vector<Object>();
        } else if (collectionType.equals(Stack.class)) {
            return new Stack<Object>();
        } else if (collectionType.equals(PriorityQueue.class)) {
            return new PriorityQueue<Object>();
        } else if (collectionType.equals(PriorityBlockingQueue.class)) {
            return new PriorityBlockingQueue<Object>();
        } else if (collectionType.equals(ArrayDeque.class)) {
            return new ArrayDeque<Object>();
        } else if (collectionType.equals(ConcurrentLinkedQueue.class)) {
            return new ConcurrentLinkedQueue<Object>();
        } else if (collectionType.equals(LinkedBlockingQueue.class)) {
            return new LinkedBlockingQueue<Object>();
        } else if (collectionType.equals(LinkedBlockingDeque.class)) {
            return new LinkedBlockingDeque<Object>();
        } else if (List.class.equals(collectionType)) {
            return new LinkedList<Object>();
        } else if (Set.class.equals(collectionType)) {
            return new HashSet<Object>();
        } else if (Queue.class.equals(collectionType)) {
            return new PriorityQueue<Object>();
        } else if (Deque.class.equals(collectionType)) {
            return new ArrayDeque<Object>();
        } else if (Collection.class.equals(collectionType)) {
            return new LinkedList<Object>();
        }
        throw new IllegalArgumentException("Unsupported collection type: " + collectionType);
    }

}
