package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class EndingWithMatcherTest {

    @Test
    public void testWhenActualIsNull() throws Exception {
        assertThat(new EndingWithMatcher().matches(null, null, ""), is(false));
    }

    @Test
    public void testWhenParameterIsNull() throws Exception {
        assertThat(new EndingWithMatcher().matches(null, "", (String) null), is(false));
    }

    @Test
    public void testWhenBothAreNull() throws Exception {
        assertThat(new EndingWithMatcher().matches(null, null, (String) null), is(false));
    }

    @Test
    public void testWhenActualDoesNotEndWithParameter() throws Exception {
        assertThat(new EndingWithMatcher().matches(null, "xyz", "abc"), is(false));
    }

    @Test
    public void testWhenActualEndsWithParameter() throws Exception {
        assertThat(new EndingWithMatcher().matches(null, "HelloWorld", "WORLD"), is(true));
    }

}