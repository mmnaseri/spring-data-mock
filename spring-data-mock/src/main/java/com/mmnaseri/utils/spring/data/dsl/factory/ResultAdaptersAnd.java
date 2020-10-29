package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;

/**
 * Lets us register an additional result adapter
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
public interface ResultAdaptersAnd extends MappingContext {

  /**
   * Registers an extra adapter
   *
   * @param adapter the adapter
   * @param <E> the type of the result for the adapter
   * @return the rest of the configuration
   */
  <E> ResultAdaptersAnd and(ResultAdapter<E> adapter);
}
