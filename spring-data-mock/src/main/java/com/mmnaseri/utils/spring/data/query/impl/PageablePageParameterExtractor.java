package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.Page;
import com.mmnaseri.utils.spring.data.query.PageParameterExtractor;
import org.springframework.data.domain.Pageable;

/**
 * This extractor will expect to see a {@link Pageable} as the last parameter passed to a query
 * method invocation, and converts that to a valid page.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/19/15)
 */
public class PageablePageParameterExtractor implements PageParameterExtractor {

  private final int index;

  public PageablePageParameterExtractor(int index) {
    this.index = index;
  }

  @Override
  public Page extract(Invocation invocation) {
    if (invocation == null) {
      throw new InvalidArgumentException("Invocation cannot be null");
    }
    final Object value = invocation.getArguments()[index];
    if (value == null) {
      throw new InvalidArgumentException("Page value should not be empty");
    }
    if (value instanceof Pageable) {
      final Pageable pageable = (Pageable) value;
      return new ImmutablePage(pageable.getPageSize(), pageable.getPageNumber());
    }
    throw new InvalidArgumentException(
        "No valid value was passed to deduce the paging description from");
  }
}
