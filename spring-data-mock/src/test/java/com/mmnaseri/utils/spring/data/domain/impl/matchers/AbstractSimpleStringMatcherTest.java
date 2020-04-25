package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Modifier;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.NotMatchingStringMatcher;
import org.testng.annotations.Test;

import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
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
        matcher.matches(new ImmutableParameter("x.y.z", null, null, null), "", "");
    }

    @Test
    public void testWhenIgnoringCase() throws Exception {
        final NotMatchingStringMatcher matcher = new NotMatchingStringMatcher();
        matcher.matches(new ImmutableParameter("x.y.z", Collections.singleton(Modifier.IGNORE_CASE), null, null),
                        "test", "TEST");
        assertThat(matcher.getActual(), is("test"));
        assertThat(matcher.getArgument(), is("test"));
    }

}