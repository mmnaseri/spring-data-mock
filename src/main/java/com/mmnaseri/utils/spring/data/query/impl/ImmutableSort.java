package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.query.Order;
import com.mmnaseri.utils.spring.data.query.Sort;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * This is an immutable sort
 *
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/17/15)
 */
public class ImmutableSort implements Sort {

    private final List<Order> orders;

    public ImmutableSort(List<Order> orders) {
        this.orders = new ArrayList<>();
        if (orders != null) {
            for (Order order : orders) {
                this.orders.add(new ImmutableOrder(order));
            }
        }
    }

    @Override
    public List<Order> getOrders() {
        return Collections.unmodifiableList(orders);
    }

}
