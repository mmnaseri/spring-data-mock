package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractSimpleMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasNoParameters() throws Exception {
        final MockSimpleMatcher matcher = new MockSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasMoreThanOneParameters() throws Exception {
        final MockSimpleMatcher matcher = new MockSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 1, 2, 3);
    }

    @Test
    public void testWhenHasOneParameter() throws Exception {
        final MockSimpleMatcher matcher = new MockSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 1);
    }

    private static class MockSimpleMatcher extends AbstractSimpleMatcher {

        @Override
        protected boolean matches(Parameter parameter, Object actual, Object expected) {
            return false;
        }

    }

}