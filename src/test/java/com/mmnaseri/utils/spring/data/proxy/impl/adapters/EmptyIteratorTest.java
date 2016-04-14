package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (4/10/16)
 */
public class EmptyIteratorTest {

    @Test
    public void testHasNext() throws Exception {
        assertThat(EmptyIterator.INSTANCE.hasNext(), is(false));
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testNext() throws Exception {
        EmptyIterator.INSTANCE.next();
    }

    @Test(expectedExceptions = IndexOutOfBoundsException.class)
    public void testRemove() throws Exception {
        EmptyIterator.INSTANCE.remove();
    }

}