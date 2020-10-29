package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class StartingWithMatcherTest {

  @Test
  public void testWhenActualIsNull() {
    assertThat(new StartingWithMatcher().matches(null, ""), is(false));
  }

  @Test
  public void testWhenParameterIsNull() {
    assertThat(new StartingWithMatcher().matches("", null), is(false));
  }

  @Test
  public void testWhenBothAreNull() {
    assertThat(new StartingWithMatcher().matches(null, null), is(false));
  }

  @Test
  public void testWhenActualDoesNotStartWithParameter() {
    assertThat(new StartingWithMatcher().matches("xyz", "abc"), is(false));
  }

  @Test
  public void testWhenActualStartsWithParameter() {
    assertThat(new StartingWithMatcher().matches("HelloWorld", "HELLO"), is(true));
  }
}
