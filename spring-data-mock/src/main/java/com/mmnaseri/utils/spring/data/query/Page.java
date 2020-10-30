package com.mmnaseri.utils.spring.data.query;

/**
 * This interface represents metadata about a pagination request
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface Page {

  /** @return the size of the page */
  int getPageSize();

  /** @return the maximum number of the items in each page */
  int getPageNumber();
}
