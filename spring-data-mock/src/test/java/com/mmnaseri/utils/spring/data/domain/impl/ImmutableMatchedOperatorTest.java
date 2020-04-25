package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.MatchedOperator;
import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.domain.impl.matchers.IsFalseMatcher;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.Random;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/17/16, 12:41 PM)
 */
public class ImmutableMatchedOperatorTest {

    private Operator original;
    private MatchedOperator matchedOperator;

    @BeforeMethod
    public void setUp() throws Exception {
        original = new ImmutableOperator("operator", new Random().nextInt(), new IsFalseMatcher(), "a", "b", "c");
        matchedOperator = new ImmutableMatchedOperator(original, "a");
    }

    @Test
    public void testDelegation() throws Exception {
        assertThat(matchedOperator.getMatcher(), is(original.getMatcher()));
        assertThat(matchedOperator.getName(), is(original.getName()));
        assertThat(matchedOperator.getOperands(), is(original.getOperands()));
        assertThat(matchedOperator.getTokens(), is(original.getTokens()));
    }

    @Test
    public void testMatchedToken() throws Exception {
        assertThat(matchedOperator.getMatchedToken(), is("a"));
    }
}