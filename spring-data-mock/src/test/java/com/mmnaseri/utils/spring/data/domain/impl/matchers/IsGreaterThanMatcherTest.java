package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/30/15)
 */
public class IsGreaterThanMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = "Expected property to be comparable: xyz")
    public void testValueIsNotComparable() throws Exception {
        new IsGreaterThanMatcher().matches(new ImmutableParameter("xyz", null, null, null), new Object(), new Object());
    }

    @Test
    public void testValuesAreEqual() throws Exception {
        assertThat(new IsGreaterThanMatcher().matches(null, 5, 5), is(false));
    }

    @Test
    public void testActualIsLessThanPivot() throws Exception {
        assertThat(new IsGreaterThanMatcher().matches(null, 1, 5), is(false));
    }

    @Test
    public void testActualIsGreaterThanPivot() throws Exception {
        assertThat(new IsGreaterThanMatcher().matches(null, 10, 5), is(true));
    }

    @Test
    public void testActualIsNull() throws Exception {
        assertThat(new IsGreaterThanMatcher().matches(null, null, 5), is(false));
    }

    @Test
    public void testPivotIsNull() throws Exception {
        assertThat(new IsGreaterThanMatcher().matches(null, 5, (Comparable) null), is(false));
    }

}