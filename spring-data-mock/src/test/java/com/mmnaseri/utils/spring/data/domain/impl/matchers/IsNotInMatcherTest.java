package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collections;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsNotInMatcherTest {

    @Test
    public void testWhenItemIsNull() throws Exception {
        assertThat(new IsNotInMatcher().matches(null, null, Collections.emptyList()), is(true));
    }

    @Test
    public void testWhenItemIsInCollection() throws Exception {
        assertThat(new IsNotInMatcher().matches(null, 1, Arrays.asList(1, 2, 3, 4)), is(false));
    }

    @Test
    public void testWhenItemIsNotInCollection() throws Exception {
        assertThat(new IsNotInMatcher().matches(null, 1, Arrays.asList(3, 4, 5, 6)), is(true));
    }

}