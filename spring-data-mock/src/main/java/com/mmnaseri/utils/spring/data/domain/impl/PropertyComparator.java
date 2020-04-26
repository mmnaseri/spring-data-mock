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
 * This is a comparator that will compare two objects based on a common property. The property should be defined as an
 * expression such as "x.y.z".
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class PropertyComparator implements Comparator<Object> {

    private static final NullHandling DEFAULT_NULL_HANDLING = NullHandling.NULLS_LAST;
    private final NullHandling nullHandling;
    private final String property;
    private final SortDirection direction;

    PropertyComparator(Order order) {
        this.nullHandling = order.getNullHandling() == null || NullHandling.DEFAULT.equals(order.getNullHandling())
                ? DEFAULT_NULL_HANDLING : order.getNullHandling();
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

    /**
     * Returns the value of the specified {@link #property property} from the object, given that it exists. Otherwise,
     * it throws an {@link InvalidArgumentException}.
     *
     * @param object the object to read the property from
     * @return the value of the property
     * @throws InvalidArgumentException
     */
    private Object safeReadPropertyValue(Object object) {
        Object firstValue;
        try {
            firstValue = PropertyUtils.getPropertyValue(object, property);
        } catch (Exception e) {
            throw new InvalidArgumentException("Failed to read property value for " + property + " on " + object, e);
        }
        return firstValue;
    }

    /**
     * If either of the two values is {@literal null}, this will compare them by taking {@link NullHandling} into
     * account.
     *
     * @param first  the first value
     * @param second the second value
     * @return comparison results as defined by {@link Comparable#compareTo(Object)}
     */
    private int compareIfEitherIsNull(Object first, Object second) {
        if (first == null && second != null) {
            return NullHandling.NULLS_FIRST.equals(nullHandling) ? -1 : 1;
        } else if (first != null) {
            return NullHandling.NULLS_FIRST.equals(nullHandling) ? 1 : -1;
        } else {
            return 0;
        }
    }

    /**
     * This will compare the two values if their are <em>compatible</em>, meaning one of them is of a type that is a
     * super type or is the same type as the other one.
     *
     * @param first  the first item
     * @param second the second item
     * @return comparison results as defined by {@link Comparable#compareTo(Object)}
     * @throws InvalidArgumentException
     */
    @SuppressWarnings("unchecked")
    private int compareIfCompatible(Object first, Object second) {
        checkForComparable(first, second);
        if (first.getClass().isInstance(second)) {
            return ((Comparable) first).compareTo(second);
        } else if (second.getClass().isInstance(first)) {
            return ((Comparable) second).compareTo(first) * -1;
        } else {
            throw new InvalidArgumentException("Values for were not of the same type for property: " + property);
        }
    }

    /**
     * This method checks to make sure both values are of type {@link Comparable}
     *
     * @param first  the first value
     * @param second the second value
     * @throws InvalidArgumentException if they are not
     */
    private void checkForComparable(Object first, Object second) {
        if (!(first instanceof Comparable) || !(second instanceof Comparable)) {
            throw new InvalidArgumentException("Expected both values to be comparable for property: " + property);
        }
    }

    /**
     * Given a collection of objects, will sort them by taking the sort property into account.
     *
     * @param collection the collection of items
     * @param sort       the sort specification
     */
    public static void sort(List<?> collection, Sort sort) {
        for (int i = sort.getOrders().size() - 1; i >= 0; i--) {
            Collections.sort(collection, new PropertyComparator(sort.getOrders().get(i)));
        }
    }

}
