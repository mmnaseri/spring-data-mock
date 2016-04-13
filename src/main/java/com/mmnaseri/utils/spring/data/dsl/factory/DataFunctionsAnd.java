package com.mmnaseri.utils.spring.data.dsl.factory;

import com.mmnaseri.utils.spring.data.query.DataFunction;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface DataFunctionsAnd extends DataStores {

    <R> DataFunctionsAnd and(String name, DataFunction<R> function);

}
