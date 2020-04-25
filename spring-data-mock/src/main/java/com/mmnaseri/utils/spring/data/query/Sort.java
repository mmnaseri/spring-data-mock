package com.mmnaseri.utils.spring.data.query;

import java.util.List;

/**
 * This interface represents a sort specification, which is in turn a collection of {@link Order orders}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface Sort {

    /**
     * @return the orders comprising this sort specification
     */
    List<Order> getOrders();

}
