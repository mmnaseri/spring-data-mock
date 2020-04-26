package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsNotLikeMatcherTest {

    @Test
    public void testWhenBothSubjectAndReferenceAreNull() {
        assertThat(new IsNotLikeMatcher().matches(null, null), is(false));
    }

    @Test

    public void testWhenSubjectIsNullAndReferenceIsNotNull() {
        assertThat(new IsNotLikeMatcher().matches(null, ""), is(true));
    }

    @Test
    public void testWhenReferenceIsNullAndSubjectIsNotNull() {
        assertThat(new IsNotLikeMatcher().matches("", null), is(true));
    }

    @Test
    public void testWhenTheyAreNotAlike() {
        assertThat(new IsNotLikeMatcher().matches("Hello World", "World"), is(true));
    }

    @Test
    public void testWhenTheyAreAlike() {
        assertThat(new IsNotLikeMatcher().matches("Hello world!", "hello WORLD!"), is(false));
    }
}