/*
 * Copyright (c) 2014 Milad Naseri
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of this
 * software and associated documentation files (the "Software"), to deal in the Software
 * without restriction, including without limitation the rights to use, copy, modify, merge,
 * publish, distribute, sublicense, and/or sell copies of the Software, and to permit persons
 * to whom the Software is furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all copies or
 * substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR IMPLIED,
 * INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY, FITNESS FOR A PARTICULAR
 * PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE
 * FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE,
 * ARISING FROM, OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.agileapes.utils.spring.impl.strategies;

import com.agileapes.utils.spring.InterceptionStrategy;
import com.agileapes.utils.spring.RepositoryDescriptor;
import com.agileapes.utils.spring.domain.QueryMethodItemMatcher;
import com.agileapes.utils.spring.domain.QueryMethodMetadata;
import com.agileapes.utils.spring.tools.EntityTools;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.repository.Repository;

import java.io.Serializable;
import java.lang.reflect.Method;
import java.lang.reflect.ParameterizedType;
import java.util.*;
import java.util.concurrent.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/23 AD, 16:56)
 */
public class SimpleLookupQueryMethodStrategy<E, K extends Serializable, R extends Repository<E, K>> implements InterceptionStrategy {

    private final Map<K, E> data;
    private final RepositoryDescriptor<E, K, R> descriptor;

    public SimpleLookupQueryMethodStrategy(Map<K, E> data, RepositoryDescriptor<E, K, R> descriptor) {
        this.data = data;
        this.descriptor = descriptor;
    }

    @Override
    public boolean intercepts(Method method) {
        if (!method.getReturnType().equals(descriptor.getEntityType())) {
            if (!Iterable.class.isAssignableFrom(method.getReturnType())) {
                return false;
            }
            if (!(method.getGenericReturnType() instanceof ParameterizedType)) {
                return false;
            }
            if (((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments().length != 1) {
                return false;
            }
            if (!((ParameterizedType) method.getGenericReturnType()).getActualTypeArguments()[0].equals(descriptor.getEntityType())) {
                return false;
            }
        }
        return method.getName().matches("^(get|find|read|query).*?By.+");
    }

    @Override
    public Object call(Object target, Method method, Object... parameters) throws Throwable {
        final QueryMethodMetadata<E, K, R> metadata = new QueryMethodMetadata<E, K, R>(method, descriptor);
        if (!metadata.isRead()) {
            throw new UnsupportedOperationException();
        }
        final QueryMethodItemMatcher<E, K, R> matcher = new QueryMethodItemMatcher<E, K, R>(metadata);
        final Collection<E> items;
        if (metadata.isDistinct()) {
            items = new HashSet<E>();
        } else {
            items = new ArrayList<E>();
        }
        for (E entity : data.values()) {
            if (matcher.matches(entity, parameters)) {
                items.add(entity);
            }
        }
        List<E> result = new ArrayList<E>(items);
        if (metadata.getSort() != null) {
            EntityTools.sort(result, metadata.getSort());
        } else if (metadata.isSorting()) {
            EntityTools.sort(result, (Sort) parameters[parameters.length - 1]);
        }
        if (metadata.getLimit() > 0) {
            result = result.subList(0, Math.min(result.size(), metadata.getLimit()));
        }
        if (metadata.isPaging()) {
            final Pageable pageable = (Pageable) parameters[parameters.length - 1];
            if (pageable.getSort() != null && metadata.getSort() != null) {
                throw new IllegalArgumentException("You cannot specify dynamic sorting while static sorting is in place");
            }
            final List<E> temp;
            if (pageable.getOffset() >= result.size()) {
                temp = Collections.emptyList();
            } else {
                temp = result.subList(pageable.getOffset(), Math.min(result.size(), pageable.getOffset() + pageable.getPageSize()));
            }
            result = temp;
        }
        if (metadata.isCollection()) {
            Collection<E> collection = getCollectionInstance(metadata.getCollectionType(), result.size());
            collection.addAll(result);
            return collection;
        } else if (metadata.isArray()) {
            return result.toArray();
        } else {
            if (result.size() > 1) {
                throw new IllegalStateException("Found more than one item");
            } else if (result.isEmpty()) {
                return null;
            } else {
                return result.get(0);
            }
        }
    }

    private Collection<E> getCollectionInstance(Class<?> collectionType, int capacity) {
        if (Iterable.class.equals(collectionType)) {
            return new ArrayList<E>();
        } else if (Collection.class.equals(collectionType)) {
            return new ArrayList<E>();
        } else if (Set.class.equals(collectionType)) {
            return new HashSet<E>();
        } else if (SortedSet.class.equals(collectionType)) {
            return new TreeSet<E>();
        } else if (NavigableSet.class.equals(collectionType)) {
            return new TreeSet<E>();
        } else if (ConcurrentSkipListSet.class.equals(collectionType)) {
            return new ConcurrentSkipListSet<E>();
        } else if (TreeSet.class.equals(collectionType)) {
            return new TreeSet<E>();
        } else if (CopyOnWriteArraySet.class.equals(collectionType)) {
            return new CopyOnWriteArraySet<E>();
        } else if (HashSet.class.equals(collectionType)) {
            return new HashSet<E>();
        } else if (LinkedHashSet.class.equals(collectionType)) {
            return new LinkedHashSet<E>();
        } else if (List.class.equals(collectionType)) {
            return new ArrayList<E>();
        } else if (ArrayList.class.equals(collectionType)) {
            return new ArrayList<E>();
        } else if (Vector.class.equals(collectionType)) {
            return new Vector<E>();
        } else if (LinkedList.class.equals(collectionType)) {
            return new LinkedList<E>();
        } else if (CopyOnWriteArrayList.class.equals(collectionType)) {
            return new CopyOnWriteArrayList<E>();
        } else if (Queue.class.equals(collectionType)) {
            return new PriorityQueue<E>();
        } else if (LinkedTransferQueue.class.equals(collectionType)) {
            return new LinkedTransferQueue<E>();
        } else if (SynchronousQueue.class.equals(collectionType)) {
            return new SynchronousQueue<E>();
        } else if (LinkedBlockingQueue.class.equals(collectionType)) {
            return new LinkedBlockingQueue<E>();
        } else if (PriorityBlockingQueue.class.equals(collectionType)) {
            return new PriorityBlockingQueue<E>();
        } else if (ArrayBlockingQueue.class.equals(collectionType)) {
            return new ArrayBlockingQueue<E>(capacity);
        } else if (LinkedBlockingQueue.class.equals(collectionType)) {
            return new LinkedBlockingQueue<E>();
        } else if (ConcurrentLinkedQueue.class.equals(collectionType)) {
            return new ConcurrentLinkedQueue<E>();
        } else if (ArrayDeque.class.equals(collectionType)) {
            return new ArrayDeque<E>();
        }
        throw new UnsupportedOperationException();
    }

}
