package com.mmnaseri.utils.spring.data.dsl.function;

import com.mmnaseri.utils.spring.data.query.DataFunctionRegistry;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public interface BuildInitializer extends FunctionRegistration {

    FunctionRegistration startingWith(DataFunctionRegistry registry);

}
