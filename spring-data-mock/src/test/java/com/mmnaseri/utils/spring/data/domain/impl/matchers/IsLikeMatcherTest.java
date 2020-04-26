package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsLikeMatcherTest {

    @Test
    public void testWhenSubjectIsNull() {
        assertThat(new IsLikeMatcher().matches(null, ""), is(false));
    }

    @Test
    public void testWhenReferenceIsNull() {
        assertThat(new IsLikeMatcher().matches("", null), is(false));
    }

    @Test
    public void testWhenTheyAreNotAlike() {
        assertThat(new IsLikeMatcher().matches("Hello World", "World"), is(false));
    }

    @Test
    public void testWhenTheyAreAlike() {
        assertThat(new IsLikeMatcher().matches("Hello world!", "hello WORLD!"), is(true));
    }
}