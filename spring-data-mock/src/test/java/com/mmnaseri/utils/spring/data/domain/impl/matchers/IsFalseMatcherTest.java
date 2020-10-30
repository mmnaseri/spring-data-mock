package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsFalseMatcherTest {

  @Test
  public void testWhenSubjectIsNull() {
    assertThat(new IsFalseMatcher().matches(null), is(false));
  }

  @Test
  public void testSubjectIsNonBoolean() {
    assertThat(new IsFalseMatcher().matches(""), is(false));
  }

  @Test
  public void testIsFalse() {
    assertThat(new IsFalseMatcher().matches(false), is(true));
  }

  @Test
  public void testIsTrue() {
    assertThat(new IsFalseMatcher().matches(true), is(false));
  }
}
