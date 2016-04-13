package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class EmptyIteratorTest {

    @Test
    public void testHasNext() throws Exception {
        assertThat(new EmptyIterator().hasNext(), is(false));
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testNext() throws Exception {
        new EmptyIterator().next();
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testRemove() throws Exception {
        new EmptyIterator().remove();
    }

}