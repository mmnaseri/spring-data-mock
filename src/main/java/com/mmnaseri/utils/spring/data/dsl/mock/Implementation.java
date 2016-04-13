package com.mmnaseri.utils.spring.data.dsl.mock;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (10/14/15)
 */
public interface Implementation extends End {

    ImplementationAnd usingImplementation(Class<?> implementation);

}
