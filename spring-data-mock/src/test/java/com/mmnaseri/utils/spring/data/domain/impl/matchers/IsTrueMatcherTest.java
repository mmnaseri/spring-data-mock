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
    public void testWhenSubjectIsNull() throws Exception {
        assertThat(new IsTrueMatcher().matches(null, null), is(false));
    }

    @Test
    public void testSubjectIsNonBoolean() throws Exception {
        assertThat(new IsTrueMatcher().matches(null, ""), is(false));
    }

    @Test
    public void testIsFalse() throws Exception {
        assertThat(new IsTrueMatcher().matches(null, false), is(false));
    }

    @Test
    public void testIsTrue() throws Exception {
        assertThat(new IsTrueMatcher().matches(null, true), is(true));
    }

}