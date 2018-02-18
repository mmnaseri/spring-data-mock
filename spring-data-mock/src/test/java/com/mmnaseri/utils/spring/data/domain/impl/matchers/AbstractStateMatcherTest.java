package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableOperator;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingStateMatcher;
import org.testng.annotations.Test;

import static org.testng.Assert.assertFalse;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class AbstractStateMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasParameters() throws Exception {
        final NotMatchingStateMatcher matcher = new NotMatchingStateMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample operator", 0, null)), new Object(), new Object());
    }

    @Test
    public void testWhenHasNoParameters() throws Exception {
        final NotMatchingStateMatcher matcher = new NotMatchingStateMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), new Object(), new Object[0]);
    }

    @Test
    public void shouldNotApplyToNonEmptyListOfParameters() {
        final NotMatchingStateMatcher matcher = new NotMatchingStateMatcher();
        assertFalse(matcher.isApplicableTo(String.class, String.class));
    }
}