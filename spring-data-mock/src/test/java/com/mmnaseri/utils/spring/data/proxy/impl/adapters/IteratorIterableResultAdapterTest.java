package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.isIn;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class IteratorIterableResultAdapterTest {

  @Test
  public void testAdapting() throws Exception {
    final IteratorIterableResultAdapter adapter = new IteratorIterableResultAdapter();
    final Iterator<?> value =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findIterator"), null),
            Arrays.asList(1, 2, 3, 4));
    assertThat(value, is(notNullValue()));
    int count = 0;
    while (value.hasNext()) {
      final Object item = value.next();
      assertThat(item, isIn(new Object[] {1, 2, 3, 4}));
      count++;
    }
    assertThat(count, is(4));
  }

  @Test
  public void testAccepting() throws Exception {
    final IteratorIterableResultAdapter adapter = new IteratorIterableResultAdapter();
    assertThat(adapter.accepts(null, null), is(false));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findOther"), new Object[] {}),
            new Object()),
        is(false));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findIterator"), new Object[] {}),
            new Object()),
        is(false));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findIterator"), new Object[] {}),
            new ArrayList<>()),
        is(true));
  }
}
