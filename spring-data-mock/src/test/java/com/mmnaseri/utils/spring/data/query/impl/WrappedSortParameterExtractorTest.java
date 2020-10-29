package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import com.mmnaseri.utils.spring.data.query.Sort;
import org.testng.annotations.Test;

import java.util.ArrayList;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class WrappedSortParameterExtractorTest {

    @Test(expectedExceptions = RepositoryDefinitionException.class)
    public void testPassingNull() {
        new WrappedSortParameterExtractor(null);
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testNullInvocation() {
        final WrappedSortParameterExtractor extractor = new WrappedSortParameterExtractor(
                new ImmutableSort(new ArrayList<>()));
        extractor.extract(null);
    }

    @Test
    public void testIdentity() {
        final Sort sort = new ImmutableSort(new ArrayList<>());
        final WrappedSortParameterExtractor extractor = new WrappedSortParameterExtractor(sort);
        assertThat(extractor.extract(new ImmutableInvocation(null, new Object[]{})), is(sort));
    }

}