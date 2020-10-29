package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.query.NullHandling;
import com.mmnaseri.utils.spring.data.query.Order;
import com.mmnaseri.utils.spring.data.query.SortDirection;

/**
 * This is an immutable order.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
@SuppressWarnings("WeakerAccess")
public class ImmutableOrder implements Order {

  private final SortDirection direction;
  private final String property;
  private final NullHandling nullHandling;

  public ImmutableOrder(Order order) {
    this(order.getDirection(), order.getProperty(), order.getNullHandling());
  }

  public ImmutableOrder(SortDirection direction, String property, NullHandling nullHandling) {
    this.direction = direction;
    this.property = property;
    this.nullHandling = nullHandling;
  }

  @Override
  public SortDirection getDirection() {
    return direction;
  }

  @Override
  public String getProperty() {
    return property;
  }

  @Override
  public NullHandling getNullHandling() {
    return nullHandling;
  }

  @Override
  public String toString() {
    return property + " " + direction;
  }
}
