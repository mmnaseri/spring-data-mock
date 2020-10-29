package com.mmnaseri.utils.spring.data.domain.impl.matchers;

/**
 * This matcher will determine if the value on the object (a string) matches the pattern being
 * passed.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class RegexMatcher extends AbstractSimpleStringMatcher {

  @Override
  protected boolean matches(String actual, String argument) {
    return actual != null && argument != null && actual.matches(argument);
  }
}
