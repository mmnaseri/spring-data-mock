package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.NullHandling;
import com.mmnaseri.utils.spring.data.query.Order;
import com.mmnaseri.utils.spring.data.query.SortDirection;
import com.mmnaseri.utils.spring.data.tools.PropertyUtils;

import java.util.Comparator;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class PropertyComparator implements Comparator<Object> {

    private static final NullHandling DEFAULT_NULL_HANDLING = NullHandling.NULLS_LAST;
    private final NullHandling nullHandling;
    private final String property;
    private final SortDirection direction;

    public PropertyComparator(Order order) {
        this.nullHandling = order.getNullHandling() == null || NullHandling.DEFAULT.equals(order.getNullHandling()) ? DEFAULT_NULL_HANDLING : order.getNullHandling();
        property = order.getProperty();
        direction = order.getDirection();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object first, Object second) {
        final Object firstValue;
        try {
            firstValue = PropertyUtils.getPropertyValue(first, property);
        } catch (Exception e) {
            throw new InvalidArgumentException("Failed to read property value for " + property + " on " + first, e);
        }
        final Object secondValue;
        try {
            secondValue = PropertyUtils.getPropertyValue(second, property);
        } catch (Exception e) {
            throw new InvalidArgumentException("Failed to read property value for " + property + " on " + second, e);
        }
        int comparison = 0;
        if (firstValue == null && secondValue != null) {
            comparison = NullHandling.NULLS_FIRST.equals(nullHandling) ? -1 : 1;
        } else if (firstValue != null && secondValue == null) {
            comparison = NullHandling.NULLS_FIRST.equals(nullHandling) ? 1 : -1;
        } else if (firstValue != null) {
            if (!(firstValue instanceof Comparable) || !(secondValue instanceof Comparable)) {
                throw new InvalidArgumentException("Expected both values to be comparable for property: " + property);
            }
            if (firstValue.getClass().isInstance(secondValue)) {
                comparison = ((Comparable) firstValue).compareTo(secondValue);
            } else if (secondValue.getClass().isInstance(firstValue)) {
                comparison = ((Comparable) secondValue).compareTo(firstValue) * -1;
            } else {
                throw new InvalidArgumentException("Values for were not of the same type for property: " + property);
            }
        }
        return comparison * (SortDirection.DESCENDING.equals(direction) ? -1 : 1);
    }

}
