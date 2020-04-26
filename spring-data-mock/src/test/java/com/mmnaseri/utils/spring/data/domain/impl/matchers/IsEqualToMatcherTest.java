package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsEqualToMatcherTest {

    @Test
    public void testWhenBothAreNull() {
        assertThat(new IsEqualToMatcher().matches(null, null, new Object[]{null}), is(true));
    }

    @Test
    public void testWhenTheyAreTheSameInstance() {
        final Object obj = new Object();
        assertThat(new IsEqualToMatcher().matches(null, obj, obj), is(true));
    }

    @Test
    public void testWhenTheyHaveTheSameValue() {
        assertThat(new IsEqualToMatcher().matches(null, 1, 1), is(true));
    }

    @Test
    public void testWhenTheyDiffer() {
        assertThat(new IsEqualToMatcher().matches(null, new Object(), new Object()), is(false));
    }

    @Test
    public void testWhenTheyHaveDifferentValues() {
        assertThat(new IsEqualToMatcher().matches(null, 1, 2), is(false));
    }

}