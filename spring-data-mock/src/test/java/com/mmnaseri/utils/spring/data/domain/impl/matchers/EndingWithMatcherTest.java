package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class EndingWithMatcherTest {

  @Test
  public void testWhenActualIsNull() {
    assertThat(new EndingWithMatcher().matches(null, ""), is(false));
  }

  @Test
  public void testWhenParameterIsNull() {
    assertThat(new EndingWithMatcher().matches("", null), is(false));
  }

  @Test
  public void testWhenBothAreNull() {
    assertThat(new EndingWithMatcher().matches(null, null), is(false));
  }

  @Test
  public void testWhenActualDoesNotEndWithParameter() {
    assertThat(new EndingWithMatcher().matches("xyz", "abc"), is(false));
  }

  @Test
  public void testWhenActualEndsWithParameter() {
    assertThat(new EndingWithMatcher().matches("HelloWorld", "WORLD"), is(true));
  }
}
