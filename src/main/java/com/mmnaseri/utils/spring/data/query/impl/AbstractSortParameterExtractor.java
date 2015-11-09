package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.query.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.NullHandling.NULLS_FIRST;
import static org.springframework.data.domain.Sort.NullHandling.NULLS_LAST;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public abstract class AbstractSortParameterExtractor implements SortParameterExtractor {

    protected Sort getSort(org.springframework.data.domain.Sort sort) {
        final List<Order> orders = new ArrayList<Order>();
        for (org.springframework.data.domain.Sort.Order order : sort) {
            final SortDirection sortDirection = order.getDirection().equals(ASC) ? SortDirection.ASCENDING : SortDirection.DESCENDING;
            final NullHandling nullHandling = order.getNullHandling().equals(NULLS_FIRST) ? NullHandling.NULLS_FIRST : (order.getNullHandling().equals(NULLS_LAST) ? NullHandling.NULLS_LAST : NullHandling.DEFAULT);
            orders.add(new ImmutableOrder(sortDirection, order.getProperty(), nullHandling));
        }
        return new ImmutableSort(orders);
    }

}
