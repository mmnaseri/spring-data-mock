package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.NullHandling;
import com.mmnaseri.utils.spring.data.query.Order;
import com.mmnaseri.utils.spring.data.query.Sort;
import com.mmnaseri.utils.spring.data.query.SortDirection;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class PropertyComparator implements Comparator<Object> {

    private static final NullHandling DEFAULT_NULL_HANDLING = NullHandling.NULLS_LAST;
    private final NullHandling nullHandling;
    private final String property;
    private final SortDirection direction;

    PropertyComparator(Order order) {
        this.nullHandling = order.getNullHandling() == null || NullHandling.DEFAULT.equals(order.getNullHandling()) ? DEFAULT_NULL_HANDLING : order.getNullHandling();
        property = order.getProperty();
        direction = order.getDirection();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object first, Object second) {
        final Object firstValue = safeReadPropertyValue(first);
        final Object secondValue = safeReadPropertyValue(second);
        int comparison;
        if (firstValue == null || secondValue == null) {
            comparison = compareIfEitherIsNull(firstValue, secondValue);
        } else {
            comparison = compareIfCompatible(firstValue, secondValue);
        }
        return comparison * (SortDirection.DESCENDING.equals(direction) ? -1 : 1);
    }

    private Object safeReadPropertyValue(Object first) {
        Object firstValue;
        try {
            firstValue = PropertyUtils.getPropertyValue(first, property);
        } catch (Exception e) {
            throw new InvalidArgumentException("Failed to read property value for " + property + " on " + first, e);
        }
        return firstValue;
    }

    private int compareIfEitherIsNull(Object firstValue, Object secondValue) {
        if (firstValue == null && secondValue != null) {
            return NullHandling.NULLS_FIRST.equals(nullHandling) ? -1 : 1;
        } else if (firstValue != null) {
            return NullHandling.NULLS_FIRST.equals(nullHandling) ? 1 : -1;
        } else {
            return 0;
        }
    }

    @SuppressWarnings("unchecked")
    private int compareIfCompatible(Object firstValue, Object secondValue) {
        checkForComparable(firstValue, secondValue);
        if (firstValue.getClass().isInstance(secondValue)) {
            return ((Comparable) firstValue).compareTo(secondValue);
        } else if (secondValue.getClass().isInstance(firstValue)) {
            return ((Comparable) secondValue).compareTo(firstValue) * -1;
        } else {
            throw new InvalidArgumentException("Values for were not of the same type for property: " + property);
        }
    }

    private void checkForComparable(Object firstValue, Object secondValue) {
        if (!(firstValue instanceof Comparable) || !(secondValue instanceof Comparable)) {
            throw new InvalidArgumentException("Expected both values to be comparable for property: " + property);
        }
    }

    public static void sort(List<?> collection, Sort sort) {
        for (int i = sort.getOrders().size() - 1; i >= 0; i--) {
            Collections.sort(collection, new PropertyComparator(sort.getOrders().get(i)));
        }
    }

}
