package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/30/15)
 */
public class IsNotLikeMatcherTest {

    @Test
    public void testWhenBothSubjectAndReferenceAreNull() throws Exception {
        assertThat(new IsNotLikeMatcher().matches(null, null, (String) null), is(false));
    }
    @Test

    public void testWhenSubjectIsNullAndReferenceIsNotNull() throws Exception {
        assertThat(new IsNotLikeMatcher().matches(null, null, ""), is(true));
    }

    @Test
    public void testWhenReferenceIsNullAndSubjectIsNotNull() throws Exception {
        assertThat(new IsNotLikeMatcher().matches(null, "", (String) null), is(true));
    }

    @Test
    public void testWhenTheyAreNotAlike() throws Exception {
        assertThat(new IsNotLikeMatcher().matches(null, "Hello World", "World"), is(true));
    }

    @Test
    public void testWhenTheyAreAlike() throws Exception {
        assertThat(new IsNotLikeMatcher().matches(null, "Hello world!", "hello WORLD!"), is(false));
    }
}