package com.mmnaseri.utils.spring.data.query;

import java.util.Set;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/29/15)
 */
public interface DataFunctionRegistry {

    void register(String name, DataFunction<?> function);

    DataFunction<?> getFunction(String name);

    Set<String> getFunctions();

}
