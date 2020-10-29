package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableOperator;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingUnaryMatcher;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractUnaryMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasParameters() {
        final NotMatchingUnaryMatcher matcher = new NotMatchingUnaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample operator", 0, null)),
                        new Object(), new Object());
    }

    @Test
    public void testWhenHasNoParameters() {
        final NotMatchingUnaryMatcher matcher = new NotMatchingUnaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object());
    }

    @Test
    public void shouldNotApplyToNonEmptyListOfParameters() {
        final NotMatchingUnaryMatcher matcher = new NotMatchingUnaryMatcher();
        assertFalse(matcher.isApplicableTo(String.class, String.class));
    }
}