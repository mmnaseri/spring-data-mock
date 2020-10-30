package com.mmnaseri.utils.spring.data.proxy;

/**
 * This interface represents a value object used for storing mapping information for a single type
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public interface TypeMapping<E> {

  /** @return the type of the mapped class */
  Class<E> getType();

  /** @return an instance of the mapped class */
  E getInstance();
}
