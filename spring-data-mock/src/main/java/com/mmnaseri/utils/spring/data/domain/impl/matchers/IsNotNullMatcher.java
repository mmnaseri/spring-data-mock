package com.mmnaseri.utils.spring.data.domain.impl.matchers;

/**
 * This matcher will determine if the value on the object is not {@literal null}
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsNotNullMatcher extends AbstractUnaryMatcher {

  @Override
  protected boolean matches(Object value) {
    return value != null;
  }
}
