package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsTrueMatcherTest {

    @Test
    public void testWhenSubjectIsNull() {
        assertThat(new IsTrueMatcher().matches(null), is(false));
    }

    @Test
    public void testSubjectIsNonBoolean() {
        assertThat(new IsTrueMatcher().matches(""), is(false));
    }

    @Test
    public void testIsFalse() {
        assertThat(new IsTrueMatcher().matches(false), is(false));
    }

    @Test
    public void testIsTrue() {
        assertThat(new IsTrueMatcher().matches(true), is(true));
    }

}