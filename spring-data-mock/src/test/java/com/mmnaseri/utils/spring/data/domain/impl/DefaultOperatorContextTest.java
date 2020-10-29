package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.error.DuplicateOperatorException;
import org.hamcrest.Matchers;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultOperatorContextTest {

  @Test
  public void testRegistering() {
    final DefaultOperatorContext context = new DefaultOperatorContext();
    final ImmutableOperator operator = new ImmutableOperator("x", 1, null, "A");
    context.register(operator);
    assertThat(context.getBySuffix("A"), is(notNullValue()));
    assertThat(context.getBySuffix("A"), Matchers.is(operator));
  }

  @Test(expectedExceptions = DuplicateOperatorException.class)
  public void testRegisteringDuplicates() {
    final DefaultOperatorContext context = new DefaultOperatorContext();
    context.register(new ImmutableOperator("x", 1, null, "X", "A"));
    context.register(new ImmutableOperator("y", 1, null, "B", "A"));
  }

  @Test
  public void testLookingForNonExistentOperator() {
    final DefaultOperatorContext context = new DefaultOperatorContext();
    assertThat(context.getBySuffix("xyz"), is(nullValue()));
  }
}
