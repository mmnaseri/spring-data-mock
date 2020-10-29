package com.mmnaseri.utils.spring.data.query;

/**
 * This interface represents metadata about an ordering based on a single property.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface Order {

  /** @return the direction of the sort */
  SortDirection getDirection();

  /** @return the property for which the ordering is taking place */
  String getProperty();

  /** @return how null values should be handled */
  NullHandling getNullHandling();
}
