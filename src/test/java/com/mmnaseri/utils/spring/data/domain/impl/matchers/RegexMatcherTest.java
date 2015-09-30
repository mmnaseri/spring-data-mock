package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class RegexMatcherTest {

    @Test
    public void testWhenActualIsNull() throws Exception {
        assertThat(new RegexMatcher().matches(null, null, ""), is(false));
    }

    @Test
    public void testWhenPatternIsNull() throws Exception {
        assertThat(new RegexMatcher().matches(null, "", (String) null), is(false));
    }

    @Test
    public void testWhenItemMatchesPatternPartially() throws Exception {
        assertThat(new RegexMatcher().matches(null, "hello", "lo"), is(false));
    }

    @Test
    public void testWhenItemDoesNotMatchPatternInAnyWay() throws Exception {
        assertThat(new RegexMatcher().matches(null, "hello", "\\d+"), is(false));
    }

    @Test
    public void testWhenItemMatchesPattern() throws Exception {
        assertThat(new RegexMatcher().matches(null, "12345", "\\d+"), is(true));
    }

}