package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.hamcrest.Matchers;
import org.springframework.util.concurrent.ListenableFuture;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class ListenableFutureIterableResultAdapterTest {

  @Test
  public void testAccepting() throws Exception {
    final ListenableFutureIterableResultAdapter adapter =
        new ListenableFutureIterableResultAdapter();
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
                ReturnTypeSampleRepository.class.getMethod("findListenableFuture"),
                new Object[] {}),
            new Object()),
        is(false));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findListenableFuture"),
                new Object[] {}),
            new ArrayList<>()),
        is(true));
  }

  @Test
  public void testAdapting() throws Exception {
    final ListenableFutureIterableResultAdapter adapter =
        new ListenableFutureIterableResultAdapter();
    final List<Integer> originalValue = Arrays.asList(1, 2, 3);
    final ListenableFuture adapted =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findListenableFuture"),
                new Object[] {}),
            originalValue);
    assertThat(adapted, is(notNullValue()));
    final Object result = adapted.get();
    assertThat(result, is(notNullValue()));
    assertThat(result, Matchers.is(originalValue));
  }
}
