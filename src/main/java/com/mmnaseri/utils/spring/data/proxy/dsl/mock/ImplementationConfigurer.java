package com.mmnaseri.utils.spring.data.proxy.dsl.mock;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/9/15)
 */
public interface ImplementationConfigurer extends BuildFinalizer {

    ImplementationConfigurerConjunction useImplementation(Class<?> implementation);

}
