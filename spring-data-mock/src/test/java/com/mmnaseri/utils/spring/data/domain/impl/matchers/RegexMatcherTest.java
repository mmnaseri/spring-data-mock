package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class RegexMatcherTest {

    @Test
    public void testWhenActualIsNull() {
        assertThat(new RegexMatcher().matches(null, ""), is(false));
    }

    @Test
    public void testWhenPatternIsNull() {
        assertThat(new RegexMatcher().matches("", null), is(false));
    }

    @Test
    public void testWhenItemMatchesPatternPartially() {
        assertThat(new RegexMatcher().matches("hello", "lo"), is(false));
    }

    @Test
    public void testWhenItemDoesNotMatchPatternInAnyWay() {
        assertThat(new RegexMatcher().matches("hello", "\\d+"), is(false));
    }

    @Test
    public void testWhenItemMatchesPattern() {
        assertThat(new RegexMatcher().matches("12345", "\\d+"), is(true));
    }

}