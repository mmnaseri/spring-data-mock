package com.mmnaseri.utils.spring.data.query;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface DataFunctionRegistry {

    void register(String name, DataFunction<?> function);

    DataFunction<?> getFunction(String name);

}
