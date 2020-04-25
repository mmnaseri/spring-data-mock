package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/29/15)
 */
public class IsBetweenMatcherTest {

    @Test
    public void testWhenActualIsNull() throws Exception {
        assertThat(new IsBetweenMatcher().matches(null, null, 1, 2), is(false));
    }

    @Test
    public void testWhenLowerBoundIsNull() throws Exception {
        assertThat(new IsBetweenMatcher().matches(null, 1, null, 2), is(false));
    }

    @Test
    public void testWhenUpperBoundIsNull() throws Exception {
        assertThat(new IsBetweenMatcher().matches(null, 1, 2, null), is(false));
    }

    @Test
    public void testWhenRangeDoesNotContainValue() throws Exception {
        assertThat(new IsBetweenMatcher().matches(null, 1, 3, 6), is(false));
    }

    @Test
    public void testWhenRangeContainsValueInclusive() throws Exception {
        assertThat(new IsBetweenMatcher().matches(null, 3, 3, 6), is(true));
        assertThat(new IsBetweenMatcher().matches(null, 6, 3, 6), is(true));
    }

    @Test
    public void testWhenRangeContainsValueMidRange() throws Exception {
        assertThat(new IsBetweenMatcher().matches(null, 4, 3, 6), is(true));
    }

}