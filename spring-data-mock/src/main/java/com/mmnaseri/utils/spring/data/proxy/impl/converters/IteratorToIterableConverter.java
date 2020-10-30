package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;

import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;

/**
 * This value will convert an iterator to an iterable.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/28/15)
 */
@SuppressWarnings("WeakerAccess")
public class IteratorToIterableConverter extends AbstractResultConverter {

  @Override
  protected Object doConvert(Invocation invocation, Object original) {
    if (original instanceof Iterator) {
      Iterator iterator = (Iterator) original;
      final List<Object> list = new LinkedList<>();
      while (iterator.hasNext()) {
        list.add(iterator.next());
      }
      return list;
    }
    return original;
  }
}
