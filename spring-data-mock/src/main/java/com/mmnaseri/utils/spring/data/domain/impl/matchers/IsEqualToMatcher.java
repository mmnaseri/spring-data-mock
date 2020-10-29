package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;

import java.util.Objects;

/**
 * This matcher will check to see if the value is strictly equal to the value passed as the argument
 * of the query method. This also includes equating null values.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/17/15)
 */
public class IsEqualToMatcher extends AbstractSimpleMatcher {

  @Override
  protected boolean matches(Parameter parameter, Object actual, Object expected) {
    return Objects.equals(actual, expected);
  }
}
