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
public class IsGreaterThanMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class,
          expectedExceptionsMessageRegExp = "Expected property to be comparable: xyz")
    public void testValueIsNotComparable() {
        new IsGreaterThanMatcher().matches(new ImmutableParameter("xyz", null, null, null), new Object(), new Object());
    }

    @Test
    public void testValuesAreEqual() {
        assertThat(new IsGreaterThanMatcher().matches(5, 5), is(false));
    }

    @Test
    public void testActualIsLessThanPivot() {
        assertThat(new IsGreaterThanMatcher().matches(1, 5), is(false));
    }

    @Test
    public void testActualIsGreaterThanPivot() {
        assertThat(new IsGreaterThanMatcher().matches(10, 5), is(true));
    }

    @Test
    public void testActualIsNull() {
        assertThat(new IsGreaterThanMatcher().matches(null, 5), is(false));
    }

    @Test
    public void testPivotIsNull() {
        assertThat(new IsGreaterThanMatcher().matches(5, null), is(false));
    }

}