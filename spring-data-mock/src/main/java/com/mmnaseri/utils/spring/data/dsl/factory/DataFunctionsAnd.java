package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.query.DataFunction;

/**
 * This interface lets you add additional functions
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface DataFunctionsAnd extends DataStores {

  /**
   * adds an additional function
   *
   * @param name the name of the function
   * @param function the function
   * @param <R> the type of the result
   * @return the rest of the configuration
   */
  <R> DataFunctionsAnd and(String name, DataFunction<R> function);
}
