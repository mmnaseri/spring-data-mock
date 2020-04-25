package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.NullHandling;
import com.mmnaseri.utils.spring.data.query.SortDirection;
import org.springframework.data.domain.Sort;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DirectSortParameterExtractorTest {

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testNullInvocation() throws Exception {
        final DirectSortParameterExtractor extractor = new DirectSortParameterExtractor(0);
        extractor.extract(null);
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testPassingNullSort() throws Exception {
        final DirectSortParameterExtractor extractor = new DirectSortParameterExtractor(0);
        extractor.extract(new ImmutableInvocation(null, new Object[]{null}));
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testPassingWrongTypeOfArgument() throws Exception {
        final DirectSortParameterExtractor extractor = new DirectSortParameterExtractor(0);
        extractor.extract(new ImmutableInvocation(null, new Object[]{new Object()}));
    }

    @Test
    public void testPassingSortValue() throws Exception {
        final DirectSortParameterExtractor extractor = new DirectSortParameterExtractor(0);
        final com.mmnaseri.utils.spring.data.query.Sort extracted = extractor.extract(
                new ImmutableInvocation(null, new Object[]{Sort.by("a", "b")}));
        assertThat(extracted, is(notNullValue()));
        assertThat(extracted.getOrders(), hasSize(2));
        assertThat(extracted.getOrders().get(0).getProperty(), is("a"));
        assertThat(extracted.getOrders().get(0).getDirection(), is(SortDirection.ASCENDING));
        assertThat(extracted.getOrders().get(0).getNullHandling(), is(NullHandling.DEFAULT));
        assertThat(extracted.getOrders().get(1).getProperty(), is("b"));
        assertThat(extracted.getOrders().get(1).getDirection(), is(SortDirection.ASCENDING));
        assertThat(extracted.getOrders().get(1).getNullHandling(), is(NullHandling.DEFAULT));
    }

}