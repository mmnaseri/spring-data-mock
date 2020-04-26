package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class ContainingMatcherTest {

    @Test
    public void testMatchingWhenActualIsNull() throws Exception {
        assertThat(new ContainingMatcher().matches(null, null, ""), is(false));
    }

    @Test
    public void testMatchingWhenParameterIsNull() throws Exception {
        assertThat(new ContainingMatcher().matches(null, "", (String) null), is(false));
    }

    @Test
    public void testMatchingWhenParameterDoesNotContainActual() throws Exception {
        assertThat(new ContainingMatcher().matches(null, "xyz", "abc"), is(false));
    }

    @Test
    public void testMatchingWhenParameterContainsActual() throws Exception {
        assertThat(new ContainingMatcher().matches(null, "HelloWorld", "owo"), is(true));
    }

}