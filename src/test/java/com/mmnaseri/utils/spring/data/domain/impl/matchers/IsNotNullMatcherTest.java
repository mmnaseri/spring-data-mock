package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/30/15)
 */
public class IsNotNullMatcherTest {

    @Test
    public void testSubjectIsNull() throws Exception {
        assertThat(new IsNotNullMatcher().matches(null, null), is(false));
    }

    @Test
    public void testSubjectIsNotNull() throws Exception {
        assertThat(new IsNotNullMatcher().matches(null, new Object()), is(true));
    }

}