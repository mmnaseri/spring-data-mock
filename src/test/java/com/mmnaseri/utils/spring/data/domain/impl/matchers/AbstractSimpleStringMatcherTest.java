package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractSimpleStringMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testValueNotAString() throws Exception {
        final MockStringMatcher matcher = new MockStringMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, "");
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testParameterNotAString() throws Exception {
        final MockStringMatcher matcher = new MockStringMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), "", 1);
    }

    @Test
    public void testWhenBothAreStrings() throws Exception {
        final MockStringMatcher matcher = new MockStringMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), "", (Object) "");
    }

    private static class MockStringMatcher extends AbstractSimpleStringMatcher {

        @Override
        protected boolean matches(Parameter parameter, String actual, String argument) {
            return false;
        }

    }

}