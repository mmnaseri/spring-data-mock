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
    public void testWhenSubjectIsNull() throws Exception {
        assertThat(new IsLikeMatcher().matches(null, null, ""), is(false));
    }

    @Test
    public void testWhenReferenceIsNull() throws Exception {
        assertThat(new IsLikeMatcher().matches(null, "", (String) null), is(false));
    }

    @Test
    public void testWhenTheyAreNotAlike() throws Exception {
        assertThat(new IsLikeMatcher().matches(null, "Hello World", "World"), is(false));
    }

    @Test
    public void testWhenTheyAreAlike() throws Exception {
        assertThat(new IsLikeMatcher().matches(null, "Hello world!", "hello WORLD!"), is(true));
    }
}