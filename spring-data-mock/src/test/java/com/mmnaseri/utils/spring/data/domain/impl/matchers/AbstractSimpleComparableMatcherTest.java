package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingSimpleComparableMatcher;
import org.testng.annotations.Test;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class AbstractSimpleComparableMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenActualIsNotComparable() throws Exception {
        final NotMatchingSimpleComparableMatcher matcher = new NotMatchingSimpleComparableMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 2);
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenPivotIsNotComparable() throws Exception {
        final NotMatchingSimpleComparableMatcher matcher = new NotMatchingSimpleComparableMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, new Object());
    }

    @Test
    public void testWhenBothAreComparable() throws Exception {
        final NotMatchingSimpleComparableMatcher matcher = new NotMatchingSimpleComparableMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, (Object) 2);
    }

}