package com.mmnaseri.utils.spring.data.proxy.impl.adapters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.proxy.ResultAdapter;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class NullToSliceResultAdapterTest {

  @Test
  public void testAcceptance() throws Exception {
    final ResultAdapter<Slice> adapter = new NullToSliceResultAdapter();
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findSlice"), new Object[] {}),
            null),
        is(true));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findPage"), new Object[] {}),
            null),
        is(true));
    assertThat(
        adapter.accepts(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findGeoPage"), new Object[] {}),
            null),
        is(true));
  }

  @Test
  public void testAdaptingToASlice() throws Exception {
    final ResultAdapter<Slice> adapter = new NullToSliceResultAdapter();
    final Slice<?> value =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findSlice"), new Object[] {}),
            null);
    assertThat(value, is(notNullValue()));
    assertThat(value.getNumber(), is(0));
    assertThat(value.getNumberOfElements(), is(0));
    assertThat(value.getSize(), is(0));
    assertThat(value.getSort(), is(Sort.unsorted()));
    assertThat(value.getContent(), hasSize(0));
  }

  @Test
  public void testAdaptingToAPage() throws Exception {
    final ResultAdapter<Slice> adapter = new NullToSliceResultAdapter();
    final Slice<?> value =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findPage"), new Object[] {}),
            null);
    assertThat(value, is(notNullValue()));
    assertThat(value.getNumber(), is(0));
    assertThat(value.getNumberOfElements(), is(0));
    assertThat(value.getSize(), is(0));
    assertThat(value.getSort(), is(Sort.unsorted()));
    assertThat(value.getContent(), hasSize(0));
  }

  @Test
  public void testAdaptingToAGeoPage() throws Exception {
    final ResultAdapter<Slice> adapter = new NullToSliceResultAdapter();
    final Slice<?> value =
        adapter.adapt(
            new ImmutableInvocation(
                ReturnTypeSampleRepository.class.getMethod("findGeoPage"), new Object[] {}),
            null);
    assertThat(value, is(notNullValue()));
    assertThat(value.getNumber(), is(0));
    assertThat(value.getNumberOfElements(), is(0));
    assertThat(value.getSize(), is(0));
    assertThat(value.getSort(), is(Sort.unsorted()));
    assertThat(value.getContent(), hasSize(0));
  }
}
