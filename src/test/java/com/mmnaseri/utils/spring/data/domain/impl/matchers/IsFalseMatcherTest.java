package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/30/15)
 */
public class IsFalseMatcherTest {

    @Test
    public void testWhenSubjectIsNull() throws Exception {
        assertThat(new IsFalseMatcher().matches(null, null), is(false));
    }

    @Test
    public void testSubjectIsNonBoolean() throws Exception {
        assertThat(new IsFalseMatcher().matches(null, ""), is(false));
    }

    @Test
    public void testIsFalse() throws Exception {
        assertThat(new IsFalseMatcher().matches(null, false), is(true));
    }

    @Test
    public void testIsTrue() throws Exception {
        assertThat(new IsFalseMatcher().matches(null, true), is(false));
    }

}