package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingBinaryComparableMatcher;
import org.testng.annotations.Test;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractBinaryComparableMatcherTest {

  @Test(
      expectedExceptions = InvalidArgumentException.class,
      expectedExceptionsMessageRegExp = ".*x.y.z.*")
  public void testWhenValueIsNotComparable() {
    final NotMatchingBinaryComparableMatcher matcher = new NotMatchingBinaryComparableMatcher();
    matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 1, 2);
  }

  @Test(
      expectedExceptions = InvalidArgumentException.class,
      expectedExceptionsMessageRegExp = ".*x.y.z.*")
  public void testWhenFirstParameterIsNotComparable() {
    final NotMatchingBinaryComparableMatcher matcher = new NotMatchingBinaryComparableMatcher();
    matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, new Object(), 2);
  }

  @Test(
      expectedExceptions = InvalidArgumentException.class,
      expectedExceptionsMessageRegExp = ".*x.y.z.*")
  public void testWhenSecondParameterIsNotComparable() {
    final NotMatchingBinaryComparableMatcher matcher = new NotMatchingBinaryComparableMatcher();
    matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, 2, new Object());
  }

  @Test
  public void testWhenAllAreComparable() {
    final NotMatchingBinaryComparableMatcher matcher = new NotMatchingBinaryComparableMatcher();
    matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, 2, 3);
  }
}
