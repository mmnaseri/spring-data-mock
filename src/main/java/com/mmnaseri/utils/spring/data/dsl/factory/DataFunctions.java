package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.query.DataFunction;
import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface DataFunctions extends DataStores {

    DataStores withDataFunctions(DataFunctionRegistry registry);

    <R> DataFunctionsAnd registerFunction(String name, DataFunction<R> function);

}
