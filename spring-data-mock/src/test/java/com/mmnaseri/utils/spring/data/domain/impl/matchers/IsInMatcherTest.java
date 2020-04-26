package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class IsInMatcherTest {

    @Test
    public void testWhenItemIsNull() throws Exception {
        assertThat(new IsInMatcher().matches(null, null, Collections.emptyList()), is(false));
    }

    @Test
    public void testWhenItemIsInCollection() throws Exception {
        assertThat(new IsInMatcher().matches(null, 1, Arrays.asList(1, 2, 3, 4)), is(true));
    }

    @Test
    public void testWhenItemIsNotInCollection() throws Exception {
        assertThat(new IsInMatcher().matches(null, 1, Arrays.asList(3, 4, 5, 6)), is(false));
    }

    @DataProvider
    public Object[][] allowedTypes() {
        return new Object[][] {
                { Collection.class },
                { Iterable.class },
                { Iterator.class }
        };
    }

    @Test(dataProvider = "allowedTypes")
    public void testAllowedParameterType(Class<?> parameterType) {
        assertTrue(new IsInMatcher().isApplicableTo(String.class, parameterType));
    }

    @Test
    public void testInvalidParameterType() {
        assertFalse(new IsInMatcher().isApplicableTo(String.class, String.class ));
    }
}