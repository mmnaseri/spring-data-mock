package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingCollectionMatcher;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.containsInAnyOrder;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/30/15)
 */
public class AbstractCollectionMatcherTest {

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = "Comparison property cannot be null: xyz")
    public void testWhenPivotIsNull() throws Exception {
        new SpyingCollectionMatcher().matches(new ImmutableParameter("xyz", null, null, null), 1, new Object[]{null});
    }

    @Test
    public void testPassingInAnArray() throws Exception {
        final SpyingCollectionMatcher matcher = new SpyingCollectionMatcher();
        matcher.matches(null, null, new Object[]{new Object[]{1, 2, 3, 4}});
        assertThat(matcher.getCollection(), is(notNullValue()));
        assertThat(matcher.getCollection(), hasSize(4));
        assertThat(matcher.getCollection(), contains((Object) 1, 2, 3, 4));
    }

    @Test
    public void testPassingInAnIterator() throws Exception {
        final SpyingCollectionMatcher matcher = new SpyingCollectionMatcher();
        matcher.matches(null, null, new Object[]{Arrays.asList(1, 2, 3, 4).iterator()});
        assertThat(matcher.getCollection(), is(notNullValue()));
        assertThat(matcher.getCollection(), hasSize(4));
        assertThat(matcher.getCollection(), contains((Object) 1, 2, 3, 4));
    }

    @Test
    public void testPassingInAnIterable() throws Exception {
        final SpyingCollectionMatcher matcher = new SpyingCollectionMatcher();
        matcher.matches(null, null, new Object[]{new HashSet<>(Arrays.asList(1, 2, 3, 4))});
        assertThat(matcher.getCollection(), is(notNullValue()));
        assertThat(matcher.getCollection(), hasSize(4));
        assertThat(matcher.getCollection(), containsInAnyOrder((Object) 1, 2, 3, 4));
    }

    @Test(expectedExceptions = InvalidArgumentException.class, expectedExceptionsMessageRegExp = "Expected an array, an iterator, or an iterable object")
    public void testPassingInAnythingElse() throws Exception {
        new SpyingCollectionMatcher().matches(null, null, new Object[]{new Object()});
    }

}