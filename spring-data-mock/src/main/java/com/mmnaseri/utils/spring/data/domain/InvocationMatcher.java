package com.mmnaseri.utils.spring.data.domain;

/**
 * This interface is defined to let us match an object to a set of predefined criteria based on the
 * values passed through a method invocation. This is used to determine whether or not an entity in
 * the data store matches the parameters passed to a query method according to the criteria defined
 * by that query method.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public interface InvocationMatcher {

  /**
   * Determines whether or not the entity matches the query
   *
   * @param entity the entity
   * @param invocation the query method invocation
   * @return {@literal true} if it was a match
   */
  boolean matches(Object entity, Invocation invocation);
}
