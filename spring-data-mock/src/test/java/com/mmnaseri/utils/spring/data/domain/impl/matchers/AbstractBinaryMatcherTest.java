package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableOperator;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingBinaryMatcher;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class AbstractBinaryMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasLessThanTwoParameters() {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)),
                        new Object());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasMoreThanTwoParameters() {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)),
                        new Object(), new Object(), new Object(), new Object());
    }

    @Test
    public void testWhenHasTwoParameters() {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        //we are creating the varargs array explicitly to call to the proper method signature
        //noinspection RedundantArrayCreation
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)),
                        new Object(), new Object[]{new Object(), new Object()});
    }

    @Test
    public void testIsApplicableTo() {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        assertTrue(matcher.isApplicableTo(String.class, String.class, String.class));
    }

    @Test
    public void shouldNotBeApplicableToOnlyOneArgument() {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        assertFalse(matcher.isApplicableTo(String.class, String.class));
    }

    @Test
    public void shouldNotBeApplicableToIncompatibleFirstArgument() {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        assertFalse(matcher.isApplicableTo(String.class, Integer.class, String.class));
    }

    @Test
    public void shouldNotBeApplicableToIncompatibleSecondArgument() {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        assertFalse(matcher.isApplicableTo(String.class, String.class, Integer.class));
    }
}