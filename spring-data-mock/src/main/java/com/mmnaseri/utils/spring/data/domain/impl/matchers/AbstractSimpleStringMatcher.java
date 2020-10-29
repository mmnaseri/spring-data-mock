package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Modifier;
import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;

/**
 * Used when the subject of the operation is a string
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public abstract class AbstractSimpleStringMatcher extends AbstractSimpleMatcher {

  @Override
  protected final boolean matches(Parameter parameter, Object actual, Object expected) {
    if (!(actual instanceof String) || !(expected instanceof String)) {
      throw new InvalidArgumentException(
          "Expected string values for property: " + parameter.getPath());
    }
    String first = (String) actual;
    String second = (String) expected;
    if (parameter.getModifiers().contains(Modifier.IGNORE_CASE)) {
      first = first.toLowerCase();
      second = second.toLowerCase();
    }
    return matches(first, second);
  }

  /**
   * Called when we want to check the expectation
   *
   * @param actual the actual value
   * @param argument the argument to the operator
   * @return {@literal true} if the match succeeded
   */
  protected abstract boolean matches(String actual, String argument);
}
