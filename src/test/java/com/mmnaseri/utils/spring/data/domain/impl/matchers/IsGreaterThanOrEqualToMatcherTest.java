package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsGreaterThanOrEqualToMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = "Expected property to be comparable: xyz")
    public void testValueIsNotComparable() throws Exception {
        new IsGreaterThanOrEqualToMatcher().matches(new ImmutableParameter("xyz", null, null, null), new Object(), new Object());
    }

    @Test
    public void testValuesAreEqual() throws Exception {
        assertThat(new IsGreaterThanOrEqualToMatcher().matches(null, 5, 5), is(true));
    }

    @Test
    public void testActualIsLessThanPivot() throws Exception {
        assertThat(new IsGreaterThanOrEqualToMatcher().matches(null, 1, 5), is(false));
    }

    @Test
    public void testActualIsGreaterThanPivot() throws Exception {
        assertThat(new IsGreaterThanOrEqualToMatcher().matches(null, 10, 5), is(true));
    }

    @Test
    public void testActualIsNull() throws Exception {
        assertThat(new IsGreaterThanOrEqualToMatcher().matches(null, null, 5), is(false));
    }

    @Test
    public void testPivotIsNull() throws Exception {
        assertThat(new IsGreaterThanOrEqualToMatcher().matches(null, 5, (Comparable) null), is(false));
    }

}