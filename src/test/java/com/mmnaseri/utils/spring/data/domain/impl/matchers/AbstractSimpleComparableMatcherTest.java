package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractSimpleComparableMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenActualIsNotComparable() throws Exception {
        final MockSimpleComparableMatcher matcher = new MockSimpleComparableMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 2);
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenPivotIsNotComparable() throws Exception {
        final MockSimpleComparableMatcher matcher = new MockSimpleComparableMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, new Object());
    }

    @Test
    public void testWhenBothAreComparable() throws Exception {
        final MockSimpleComparableMatcher matcher = new MockSimpleComparableMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, (Object) 2);
    }

    private static class MockSimpleComparableMatcher extends AbstractSimpleComparableMatcher {

        @Override
        protected boolean matches(Parameter parameter, Comparable actual, Comparable pivot) {
            return false;
        }

    }

}