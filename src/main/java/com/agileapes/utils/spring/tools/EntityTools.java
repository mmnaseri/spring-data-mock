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

package com.agileapes.utils.spring.tools;

import org.springframework.beans.BeanWrapper;
import org.springframework.beans.BeanWrapperImpl;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (2015/1/23 AD, 12:47)
 */
public abstract class EntityTools {

    public static <E, S extends E> List<S> iterableToList(Iterable<S> iterable) {
        final ArrayList<S> entities = new ArrayList<S>();
        for (S entity : iterable) {
            entities.add(entity);
        }
        return entities;
    }

    public static <E> void sort(List<E> entities, final Sort sort) {
        if (sort == null) {
            return;
        }
        Collections.sort(entities, new Comparator<E>() {
            @Override
            public int compare(E firstObject, E secondObject) {
                final BeanWrapper first = new BeanWrapperImpl(firstObject);
                final BeanWrapper second = new BeanWrapperImpl(secondObject);
                for (Sort.Order order : sort) {
                    int comparison;
                    final Object firstPropertyValue = first.getPropertyValue(order.getProperty());
                    final Object secondPropertyValue = second.getPropertyValue(order.getProperty());
                    if (firstPropertyValue == null && secondPropertyValue == null) {
                        continue;
                    }
                    if (firstPropertyValue == null) {
                        if (order.getNullHandling().equals(Sort.NullHandling.NULLS_LAST)) {
                            comparison = order.isAscending() ? -1 : 1;
                        } else {
                            comparison = order.isAscending() ? 1 : -1;
                        }
                        return comparison;
                    }
                    if (secondPropertyValue == null) {
                        if (order.getNullHandling().equals(Sort.NullHandling.NULLS_LAST)) {
                            comparison = order.isAscending() ? 1 : -1;
                        } else {
                            comparison = order.isAscending() ? -1 : 1;
                        }
                        return comparison;
                    }
                    if ((firstPropertyValue instanceof CharSequence && secondPropertyValue instanceof CharSequence)
                            || (firstPropertyValue instanceof Character && secondPropertyValue instanceof Character)) {
                        String firstCharSequence = firstPropertyValue.toString();
                        String secondCharSequence = secondPropertyValue.toString();
                        if (order.isIgnoreCase()) {
                            firstCharSequence = firstCharSequence.toLowerCase();
                            secondCharSequence = secondCharSequence.toLowerCase();
                        }
                        if (order.isAscending()) {
                            comparison = firstCharSequence.compareTo(secondCharSequence);
                        } else {
                            comparison = secondCharSequence.compareTo(firstCharSequence);
                        }
                        if (comparison != 0) {
                            return comparison;
                        } else {
                            continue;
                        }
                    }
                    if (firstPropertyValue instanceof Comparable && secondPropertyValue instanceof Comparable) {
                        Comparable firstComparable = (Comparable) firstPropertyValue;
                        Comparable secondComparable = (Comparable) secondPropertyValue;
                        if (order.isAscending()) {
                            //noinspection unchecked
                            comparison = firstComparable.compareTo(secondComparable);
                        } else {
                            //noinspection unchecked
                            comparison = secondComparable.compareTo(firstComparable);
                        }
                        if (comparison != 0) {
                            return comparison;
                        } else {
                            continue;
                        }
                    }
                    throw new IllegalArgumentException("Property " + order.getProperty() + " is not comparable");
                }
                return 0;
            }
        });
    }

}
