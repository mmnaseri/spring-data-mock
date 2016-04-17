package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableOperator;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingBinaryMatcher;
import org.testng.annotations.Test;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class AbstractBinaryMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasLessThanTwoParameters() throws Exception {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)), new Object());
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = ".*x.y.z.*")
    public void testWhenHasMoreThanTwoParameters() throws Exception {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)), new Object(), new Object(), new Object(), new Object());
    }

    @Test
    public void testWhenHasTwoParameters() throws Exception {
        final NotMatchingBinaryMatcher matcher = new NotMatchingBinaryMatcher();
        //we are creating the varargs array explicitly to call to the proper method signature
        //noinspection RedundantArrayCreation
        matcher.matches(new ImmutableParameter("x.y.z", null, null, new ImmutableOperator("sample", 2, null)), new Object(), new Object[]{new Object(), new Object()});
    }

}