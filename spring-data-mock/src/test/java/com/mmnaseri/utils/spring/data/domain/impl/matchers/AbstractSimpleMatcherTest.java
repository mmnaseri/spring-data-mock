package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingSimpleMatcher;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractSimpleMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasNoParameters() {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasMoreThanOneParameters() {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 1, 2, 3);
    }

    @Test
    public void testWhenHasOneParameter() {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), 1);
    }

    @Test
    public void testApplicableToSameType() {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        assertTrue(matcher.isApplicableTo(String.class, String.class));
    }

    @Test
    public void testApplicableToSubType() {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        assertTrue(matcher.isApplicableTo(Number.class, Integer.class));
    }

    @Test
    public void testNonApplicableToDifferentType() {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        assertFalse(matcher.isApplicableTo(String.class, Integer.class));
    }

    @Test
    public void shouldNotBeApplicableToDifferentNumberOfArguments() {
        final NotMatchingSimpleMatcher matcher = new NotMatchingSimpleMatcher();
        assertFalse(matcher.isApplicableTo(String.class, String.class, String.class));
    }
}