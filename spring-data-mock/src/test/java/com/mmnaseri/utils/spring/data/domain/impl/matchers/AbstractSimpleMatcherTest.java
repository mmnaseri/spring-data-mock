package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingSimpleMatcher;
import org.testng.annotations.Test;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractSimpleMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasNoParameters() throws Exception {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasMoreThanOneParameters() throws Exception {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 1, 2, 3);
    }

    @Test
    public void testWhenHasOneParameter() throws Exception {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 1);
    }

}