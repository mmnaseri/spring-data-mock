package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import java.util.LinkedList;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class CountDataFunctionTest {

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testThatNullSelectionResultsInError() throws Exception {
        final CountDataFunction function = new CountDataFunction();
        function.apply(null, null, null, null);
    }

    @Test
    public void testThatItReflectsTheSizeOfTheCollection() throws Exception {
        final CountDataFunction function = new CountDataFunction();
        final List<Object> selection = new LinkedList<>();
        for (int i = 0; i < 10; i++) {
            final Long count = function.apply(null, null, null, selection);
            assertThat(count, Matchers.is((long) selection.size()));
            selection.add(new Object());
        }
    }

}