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
