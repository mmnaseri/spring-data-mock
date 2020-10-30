package com.mmnaseri.utils.spring.data.query;

/**
 * This enum is used to indicate how null values should be handled when compared to non-null values.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public enum NullHandling {

  /** The null values should come before non-null values. */
  NULLS_FIRST,
  /** The null values should appear last when compared to non-null values. */
  NULLS_LAST,
  /** Pick one of the above as per the specifications of the underlying data store. */
  DEFAULT
}
