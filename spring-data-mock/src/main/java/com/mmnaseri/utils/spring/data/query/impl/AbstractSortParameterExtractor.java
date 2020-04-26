package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.query.*;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.data.domain.Sort.Direction.ASC;
import static org.springframework.data.domain.Sort.NullHandling.NULLS_FIRST;
import static org.springframework.data.domain.Sort.NullHandling.NULLS_LAST;

/**
 * This class will provide support for converting {@link org.springframework.data.domain.Sort Spring Data sort objects}
 * into {@link Sort sort objects} defined within this framework.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
@SuppressWarnings("WeakerAccess")
public abstract class AbstractSortParameterExtractor implements SortParameterExtractor {

    /**
     * Given a sort parameter from the Spring Data framework, will determine the appropriate sort metadata compatible
     * with this framework
     *
     * @param sort the sort specification
     * @return converted sort metadata
     */
    protected Sort getSort(org.springframework.data.domain.Sort sort) {
        final List<Order> orders = new ArrayList<>();
        for (org.springframework.data.domain.Sort.Order order : sort) {
            final SortDirection sortDirection = order.getDirection().equals(ASC) ? SortDirection.ASCENDING
                    : SortDirection.DESCENDING;
            final NullHandling nullHandling = order.getNullHandling().equals(NULLS_FIRST) ? NullHandling.NULLS_FIRST
                    : (order.getNullHandling().equals(NULLS_LAST) ? NullHandling.NULLS_LAST : NullHandling.DEFAULT);
            orders.add(new ImmutableOrder(sortDirection, order.getProperty(), nullHandling));
        }
        return new ImmutableSort(orders);
    }

}
