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
    public void testSubjectIsNull() {
        assertThat(new IsNullMatcher().matches(null), is(true));
    }

    @Test
    public void testSubjectIsNotNull() {
        assertThat(new IsNullMatcher().matches(new Object()), is(false));
    }

}