package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.Operator;
import com.mmnaseri.utils.spring.data.error.DuplicateOperatorException;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultOperatorContextTest {

    @Test
    public void testRegistering() throws Exception {
        final DefaultOperatorContext context = new DefaultOperatorContext();
        final ImmutableOperator operator = new ImmutableOperator("x", 1, null, "A");
        context.register(operator);
        assertThat(context.getBySuffix("A"), is(notNullValue()));
        assertThat(context.getBySuffix("A"), Matchers.<Operator>is(operator));
    }

    @Test(expectedExceptions = DuplicateOperatorException.class)
    public void testRegisteringDuplicates() throws Exception {
        final DefaultOperatorContext context = new DefaultOperatorContext();
        context.register(new ImmutableOperator("x", 1, null, "X", "A"));
        context.register(new ImmutableOperator("y", 1, null, "B", "A"));
    }

    @Test
    public void testLookingForNonExistentOperator() throws Exception {
        final DefaultOperatorContext context = new DefaultOperatorContext();
        assertThat(context.getBySuffix("xyz"), is(nullValue()));
    }

}