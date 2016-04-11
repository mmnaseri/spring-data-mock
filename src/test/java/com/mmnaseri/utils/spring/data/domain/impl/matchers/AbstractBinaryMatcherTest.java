package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableOperator;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractBinaryMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasLessThanTwoParameters() throws Exception {
        final MockBinaryMatcher matcher = new MockBinaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)), new Object());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasMoreThanTwoParameters() throws Exception {
        final MockBinaryMatcher matcher = new MockBinaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)), new Object(), new Object(), new Object(), new Object());
    }

    @Test
    public void testWhenHasTwoParameters() throws Exception {
        final MockBinaryMatcher matcher = new MockBinaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)), new Object(), new Object[]{new Object(), new Object()});
    }

    private static class MockBinaryMatcher extends AbstractBinaryMatcher {

        @Override
        protected boolean matches(Parameter parameter, Object value, Object first, Object second) {
            return false;
        }

    }

}