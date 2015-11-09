package com.mmnaseri.utils.spring.data.domain.impl.matchers;

import com.mmnaseri.utils.spring.data.domain.Parameter;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableParameter;
import org.testng.annotations.Test;

import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class AbstractCollectionMatcherTest {

    public static class PreservingCollectionMatcher extends AbstractCollectionMatcher {

        private Collection collection;

        @Override
        protected boolean matches(Parameter parameter, Object actual, Collection collection) {
            this.collection = collection;
            return false;
        }

        public Collection<?> getCollection() {
            return collection;
        }

    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Comparison property cannot be null: xyz")
    public void testWhenPivotIsNull() throws Exception {
        new PreservingCollectionMatcher().matches(new ImmutableParameter("xyz", null, null, null), 1, new Object[]{null});
    }

    @Test
    public void testPassingInAnArray() throws Exception {
        final PreservingCollectionMatcher matcher = new PreservingCollectionMatcher();
        matcher.matches(null, null, new Object[]{new Object[]{1, 2, 3, 4}});
        assertThat(matcher.getCollection(), is(notNullValue()));
        assertThat(matcher.getCollection(), hasSize(4));
        assertThat(matcher.getCollection(), contains((Object) 1, 2, 3, 4));
    }

    @Test
    public void testPassingInAnIterator() throws Exception {
        final PreservingCollectionMatcher matcher = new PreservingCollectionMatcher();
        matcher.matches(null, null, new Object[]{Arrays.asList(1, 2, 3, 4).iterator()});
        assertThat(matcher.getCollection(), is(notNullValue()));
        assertThat(matcher.getCollection(), hasSize(4));
        assertThat(matcher.getCollection(), contains((Object) 1, 2, 3, 4));
    }

    @Test
    public void testPassingInAnIterable() throws Exception {
        final PreservingCollectionMatcher matcher = new PreservingCollectionMatcher();
        matcher.matches(null, null, new Object[]{new HashSet<Integer>(Arrays.asList(1, 2, 3, 4))});
        assertThat(matcher.getCollection(), is(notNullValue()));
        assertThat(matcher.getCollection(), hasSize(4));
        assertThat(matcher.getCollection(), containsInAnyOrder((Object) 1, 2, 3, 4));
    }

    @Test(expectedExceptions = IllegalArgumentException.class, expectedExceptionsMessageRegExp = "Expected an array, an iterator, or an iterable object")
    public void testPassingInAnythingElse() throws Exception {
        new PreservingCollectionMatcher().matches(null, null, new Object[]{new Object()});
    }

}