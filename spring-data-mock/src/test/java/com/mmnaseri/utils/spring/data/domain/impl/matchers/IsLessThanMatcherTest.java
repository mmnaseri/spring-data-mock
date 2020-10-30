package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsLessThanMatcherTest {

  @Test(
      expectedExceptions = InvalidArgumentException.class,
      expectedExceptionsMessageRegExp = "Expected property to be comparable: xyz")
  public void testValueIsNotComparable() {
    new IsLessThanMatcher()
        .matches(new ImmutableParameter("xyz", null, null, null), new Object(), new Object());
  }

  @Test
  public void testValuesAreEqual() {
    assertThat(new IsLessThanMatcher().matches(5, 5), is(false));
  }

  @Test
  public void testActualIsLessThanPivot() {
    assertThat(new IsLessThanMatcher().matches(1, 5), is(true));
  }

  @Test
  public void testActualIsGreaterThanPivot() {
    assertThat(new IsLessThanMatcher().matches(10, 5), is(false));
  }

  @Test
  public void testActualIsNull() {
    assertThat(new IsLessThanMatcher().matches(null, 5), is(false));
  }

  @Test
  public void testPivotIsNull() {
    assertThat(new IsLessThanMatcher().matches(5, null), is(false));
  }
}
