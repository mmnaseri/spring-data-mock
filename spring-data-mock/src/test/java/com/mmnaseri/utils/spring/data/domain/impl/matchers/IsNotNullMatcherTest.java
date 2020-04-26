package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsNotNullMatcherTest {

    @Test
    public void testSubjectIsNull() {
        assertThat(new IsNotNullMatcher().matches(null), is(false));
    }

    @Test
    public void testSubjectIsNotNull() {
        assertThat(new IsNotNullMatcher().matches(new Object()), is(true));
    }

}