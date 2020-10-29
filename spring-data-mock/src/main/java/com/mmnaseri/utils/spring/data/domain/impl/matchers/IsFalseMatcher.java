package com.mmnaseri.utils.spring.data.domain.impl.matchers;

/**
 * This will check to see if the value on the entity object is {@literal false}.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsFalseMatcher extends AbstractUnaryMatcher {

  @Override
  protected boolean matches(Object value) {
    return Boolean.FALSE.equals(value);
  }
}
