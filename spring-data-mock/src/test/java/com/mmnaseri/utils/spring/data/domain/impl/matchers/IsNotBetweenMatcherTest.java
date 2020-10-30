package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsNotBetweenMatcherTest {

  @Test
  public void testWhenActualIsNull() {
    assertThat(new IsNotBetweenMatcher().matches(null, 1, 2), is(true));
  }

  @Test
  public void testWhenLowerBoundIsNull() {
    assertThat(new IsNotBetweenMatcher().matches(1, null, 2), is(false));
  }

  @Test
  public void testWhenUpperBoundIsNull() {
    assertThat(new IsNotBetweenMatcher().matches(1, 2, null), is(false));
  }

  @Test
  public void testWhenValueIsBelowRange() {
    assertThat(new IsNotBetweenMatcher().matches(1, 3, 6), is(true));
  }

  @Test
  public void testWhenValueIsAboveRange() {
    assertThat(new IsNotBetweenMatcher().matches(9, 3, 6), is(true));
  }

  @Test
  public void testWhenRangeContainsValueInclusive() {
    assertThat(new IsNotBetweenMatcher().matches(3, 3, 6), is(false));
    assertThat(new IsNotBetweenMatcher().matches(6, 3, 6), is(false));
  }

  @Test
  public void testWhenRangeContainsValueMidRange() {
    assertThat(new IsNotBetweenMatcher().matches(4, 3, 6), is(false));
  }
}
