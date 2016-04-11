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
public class AbstractStateMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasParameters() throws Exception {
        final MockStateMatcher matcher = new MockStateMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample operator", 0, null)), new Object(), new Object());
    }

    @Test
    public void testWhenHasNoParameters() throws Exception {
        final MockStateMatcher matcher = new MockStateMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), new Object[0]);
    }

    private static class MockStateMatcher extends AbstractStateMatcher {

        @Override
        protected boolean matches(Parameter parameter, Object value) {
            return false;
        }

    }

}