package com.mmnaseri.utils.spring.data.proxy.dsl.function;

import com.mmnaseri.utils.spring.data.query.DataFunction;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/12/15)
 */
public interface FunctionRegistrationConjunction extends BuildFinalizer {

    <R> FunctionRegistrationConjunction and(String name, DataFunction<R> function);

}
