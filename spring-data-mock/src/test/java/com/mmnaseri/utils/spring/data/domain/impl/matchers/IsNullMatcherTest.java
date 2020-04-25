package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsNullMatcherTest {

    @Test
    public void testSubjectIsNull() throws Exception {
        assertThat(new IsNullMatcher().matches(null, null), is(true));
    }

    @Test
    public void testSubjectIsNotNull() throws Exception {
        assertThat(new IsNullMatcher().matches(null, new Object()), is(false));
    }

}