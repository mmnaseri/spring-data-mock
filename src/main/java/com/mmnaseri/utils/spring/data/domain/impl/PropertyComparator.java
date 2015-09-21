package com.mmnaseri.utils.spring.data.domain.impl;

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
    private final Order order;
    private final NullHandling nullHandling;

    public PropertyComparator(Order order) {
        this.order = order;
        this.nullHandling = order.getNullHandling() == null || NullHandling.DEFAULT.equals(order.getNullHandling()) ? DEFAULT_NULL_HANDLING : order.getNullHandling();
    }

    @SuppressWarnings("unchecked")
    @Override
    public int compare(Object first, Object second) {
        final Object firstValue = PropertyUtils.getPropertyValue(first, order.getProperty());
        final Object secondValue = PropertyUtils.getPropertyValue(second, order.getProperty());
        int comparison = 0;
        if (firstValue == null && secondValue != null) {
            comparison = nullHandling.equals(NullHandling.NULLS_FIRST) ? -1 : 1;
        } else if (firstValue != null && secondValue == nullHandling) {
            comparison = nullHandling.equals(NullHandling.NULLS_FIRST) ? 1 : -1;
        } else if (firstValue != null && secondValue != null) {
            if (!(firstValue instanceof Comparable) || !(secondValue instanceof Comparable)) {
                throw new IllegalStateException("Cannot compare values for property: " + order.getProperty());
            }
            if (firstValue.getClass().isInstance(secondValue)) {
                comparison = ((Comparable) firstValue).compareTo(secondValue);
            } else if (secondValue.getClass().isInstance(firstValue)) {
                comparison = ((Comparable) secondValue).compareTo(firstValue) * -1;
            } else {
                throw new IllegalStateException("Values for were not of the same type for property: " + order.getProperty());
            }
        }
        return comparison * (order.getDirection().equals(SortDirection.DESCENDING) ? -1 : 1);
    }

}
