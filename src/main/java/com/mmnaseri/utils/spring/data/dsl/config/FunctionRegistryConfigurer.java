package com.mmnaseri.utils.spring.data.dsl.config;

import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public interface FunctionRegistryConfigurer extends DataStoreConfigurer {

    DataStoreConfigurer withFunctions(DataFunctionRegistry functionRegistry);

}
