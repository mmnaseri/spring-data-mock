package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;

/**
 * This interface lets us tell the builder how the data functions should be configured
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface DataFunctions extends DataStores {

  /**
   * Tells the builder which data function registry it should use
   *
   * @param registry the registry that should be used
   * @return the rest of the configuration
   */
  DataStores withDataFunctions(DataFunctionRegistry registry);

  /**
   * Registers a function and lets you add more functions
   *
   * @param name the name under which this function is recognized
   * @param function the function
   * @param <R> the type of the result
   * @return the rest of the configuration
   */
  <R> DataFunctionsAnd registerFunction(String name, DataFunction<R> function);
}
