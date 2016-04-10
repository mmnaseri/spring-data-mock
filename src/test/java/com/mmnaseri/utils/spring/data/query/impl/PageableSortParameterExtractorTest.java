package com.mmnaseri.utils.spring.data.query.impl;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.error.InvalidArgumentException;
import com.mmnaseri.utils.spring.data.query.NullHandling;
import com.mmnaseri.utils.spring.data.query.Sort;
import com.mmnaseri.utils.spring.data.query.SortDirection;
import org.springframework.data.domain.PageRequest;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class PageableSortParameterExtractorTest {

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testNullInvocation() throws Exception {
        final PageableSortParameterExtractor extractor = new PageableSortParameterExtractor(0);
        extractor.extract(null);
    }


    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testPassingNullValue() throws Exception {
        final PageableSortParameterExtractor extractor = new PageableSortParameterExtractor(0);
        extractor.extract(new ImmutableInvocation(null, new Object[]{null}));
    }

    @Test(expectedExceptions = InvalidArgumentException.class)
    public void testPassingWrongType() throws Exception {
        final PageableSortParameterExtractor extractor = new PageableSortParameterExtractor(0);
        extractor.extract(new ImmutableInvocation(null, new Object[]{new Object()}));
    }

    @Test
    public void testPassingPageableWithNullSort() throws Exception {
        final PageableSortParameterExtractor extractor = new PageableSortParameterExtractor(0);
        final Sort extracted = extractor.extract(new ImmutableInvocation(null, new Object[]{new PageRequest(0, 1)}));
        assertThat(extracted, is(nullValue()));
    }

    @Test
    public void testPassingPageableWithNullProperSort() throws Exception {
        final PageableSortParameterExtractor extractor = new PageableSortParameterExtractor(0);
        final Sort extracted = extractor.extract(new ImmutableInvocation(null, new Object[]{new PageRequest(0, 1, org.springframework.data.domain.Sort.Direction.DESC, "a", "b")}));
        assertThat(extracted, is(notNullValue()));
        assertThat(extracted.getOrders(), hasSize(2));
        assertThat(extracted.getOrders().get(0).getProperty(), is("a"));
        assertThat(extracted.getOrders().get(0).getDirection(), is(SortDirection.DESCENDING));
        assertThat(extracted.getOrders().get(0).getNullHandling(), is(NullHandling.DEFAULT));
        assertThat(extracted.getOrders().get(1).getProperty(), is("b"));
        assertThat(extracted.getOrders().get(1).getDirection(), is(SortDirection.DESCENDING));
        assertThat(extracted.getOrders().get(1).getNullHandling(), is(NullHandling.DEFAULT));
    }

}