package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class StartingWithMatcherTest {

    @Test
    public void testWhenActualIsNull() throws Exception {
        assertThat(new StartingWithMatcher().matches(null, null, ""), is(false));
    }

    @Test
    public void testWhenParameterIsNull() throws Exception {
        assertThat(new StartingWithMatcher().matches(null, "", (String) null), is(false));
    }

    @Test
    public void testWhenBothAreNull() throws Exception {
        assertThat(new StartingWithMatcher().matches(null, null, (String) null), is(false));
    }

    @Test
    public void testWhenActualDoesNotStartWithParameter() throws Exception {
        assertThat(new StartingWithMatcher().matches(null, "xyz", "abc"), is(false));
    }

    @Test
    public void testWhenActualStartsWithParameter() throws Exception {
        assertThat(new StartingWithMatcher().matches(null, "HelloWorld", "HELLO"), is(true));
    }

}