package com.mmnaseri.utils.spring.data.dsl.factory;

/**
 * This interface creates a branch in the grammar that lets you either configure the operators or the query description
 * extractor.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/14/15)
 */
@SuppressWarnings("WeakerAccess")
public interface QueryDescriptionConfigurer extends QueryDescription, Operators {

}
