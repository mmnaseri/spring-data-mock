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
    public void testMatchingWhenActualIsNull() {
        assertThat(new ContainingMatcher().matches(null, ""), is(false));
    }

    @Test
    public void testMatchingWhenParameterIsNull() {
        assertThat(new ContainingMatcher().matches("", null), is(false));
    }

    @Test
    public void testMatchingWhenParameterDoesNotContainActual() {
        assertThat(new ContainingMatcher().matches("xyz", "abc"), is(false));
    }

    @Test
    public void testMatchingWhenParameterContainsActual() {
        assertThat(new ContainingMatcher().matches("HelloWorld", "owo"), is(true));
    }

}