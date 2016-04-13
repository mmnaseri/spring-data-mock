package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingStringMatcher;
import org.testng.annotations.Test;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractSimpleStringMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testValueNotAString() throws Exception {
        final NotMatchingStringMatcher matcher = new NotMatchingStringMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), 1, "");
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testParameterNotAString() throws Exception {
        final NotMatchingStringMatcher matcher = new NotMatchingStringMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), "", 1);
    }

    @Test
    public void testWhenBothAreStrings() throws Exception {
        final NotMatchingStringMatcher matcher = new NotMatchingStringMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), "", (Object) "");
    }

}