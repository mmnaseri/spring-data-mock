package com.mmnaseri.utils.spring.data.domain.impl.matchers;

/**
 * This comparing matcher will determine if the passed value is smaller or equal to the first passed
 * argument and larger or equal to the second passed argument, thus determining if it does not fall
 * between the two values (if they are in in ascending order themselves).
 *
 * <p><strong>NB</strong>: This matcher does not check whether or not the two values passed are in
 * the right order, as a normal database wouldn't. If you need this functionality, you will need to
 * define a new {@link com.mmnaseri.utils.spring.data.domain.Operator operator} and add your own
 * matcher.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsNotBetweenMatcher extends AbstractBinaryComparableMatcher {

  @Override
  protected boolean matches(Comparable value, Comparable first, Comparable second) {
    //noinspection unchecked
    return first != null
        && second != null
        && (value == null || first.compareTo(value) > 0 || second.compareTo(value) < 0);
  }
}
