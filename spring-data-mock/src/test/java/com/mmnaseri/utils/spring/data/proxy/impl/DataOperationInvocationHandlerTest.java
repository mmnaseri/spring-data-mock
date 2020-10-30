package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.domain.RepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableRepositoryMetadata;
import com.mmnaseri.utils.spring.data.domain.impl.key.UUIDKeyGenerator;
import com.mmnaseri.utils.spring.data.proxy.InvocationMapping;
import com.mmnaseri.utils.spring.data.proxy.RepositoryConfiguration;
import com.mmnaseri.utils.spring.data.sample.mocks.SpyingOperation;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.repositories.SimplePersonRepository;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import com.mmnaseri.utils.spring.data.store.impl.MemoryDataStore;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.nullValue;

public class DataOperationInvocationHandlerTest {

  private DataOperationInvocationHandler<String, Person> handler;
  private List<InvocationMapping<String, Person>> mappings;

  @BeforeMethod
  public void setUp() {
    final RepositoryMetadata repositoryMetadata =
        new ImmutableRepositoryMetadata(
            String.class, Person.class, SimplePersonRepository.class, "id");
    final RepositoryConfiguration repositoryConfiguration =
        new ImmutableRepositoryConfiguration(
            repositoryMetadata, new UUIDKeyGenerator(), Collections.emptyList());
    final MemoryDataStore<String, Person> dataStore = new MemoryDataStore<>(Person.class);
    final DefaultResultAdapterContext adapterContext = new DefaultResultAdapterContext();
    final NonDataOperationInvocationHandler invocationHandler =
        new NonDataOperationInvocationHandler();
    mappings = new ArrayList<>();
    handler =
        new DataOperationInvocationHandler<>(
            repositoryConfiguration, mappings, dataStore, adapterContext, invocationHandler);
  }

  /**
   * Regression test to reproduce #12
   *
   * @throws Throwable in case the method can't be found.
   */
  @Test
  public void testCallingHashCode() throws Throwable {
    final Object proxy = new Object();
    final Object result =
        handler.invoke(proxy, Object.class.getMethod("hashCode"), new Object[] {});
    assertThat(result, is(notNullValue()));
    assertThat(result, is(proxy.hashCode()));
  }

  /** Regression test to reproduce #12 */
  @Test
  public void testCallingEqualsWhenTheyAreIdentical() throws Throwable {
    final Object proxy = new Object();
    final Object result =
        handler.invoke(proxy, Object.class.getMethod("equals", Object.class), new Object[] {proxy});
    assertThat(result, is(notNullValue()));
    assertThat(result, is(instanceOf(Boolean.class)));
    //noinspection ConstantConditions
    assertThat(result, is(true));
  }

  /** Regression test to reproduce #12 */
  @Test
  public void testCallingEqualsWhenTheyAreNotIdentical() throws Throwable {
    final Object proxy = new Object();
    final Object result =
        handler.invoke(
            proxy, Object.class.getMethod("equals", Object.class), new Object[] {new Object()});
    assertThat(result, is(notNullValue()));
    assertThat(result, is(false));
  }

  /** Regression test to reproduce #12 */
  @Test
  public void testCallingToString() throws Throwable {
    final Object proxy = new Object();
    final Object result =
        handler.invoke(proxy, Object.class.getMethod("toString"), new Object[] {});
    assertThat(result, is(notNullValue()));
    assertThat(result, is(proxy.toString()));
  }

  @Test
  public void testMissingMethodTwice() throws Throwable {
    assertThat(
        handler.invoke(new Object(), Object.class.getMethod("toString"), new Object[] {}),
        is(notNullValue()));
    assertThat(
        handler.invoke(new Object(), Object.class.getMethod("toString"), new Object[] {}),
        is(notNullValue()));
  }

  @Test
  public void testInvokingBoundMapping() throws Throwable {
    final Object originalValue = new Object();
    final SpyingOperation spy = new SpyingOperation(originalValue);
    mappings.add(
        new ImmutableInvocationMapping<>(
            ReturnTypeSampleRepository.class.getMethod("findOther"), spy));
    final Object[] args = {1, 2, 3};
    final Object result =
        handler.invoke(new Object(), ReturnTypeSampleRepository.class.getMethod("findOther"), args);
    assertThat(spy.getInvocation(), is(notNullValue()));
    assertThat(
        spy.getInvocation().getMethod(),
        is(ReturnTypeSampleRepository.class.getMethod("findOther")));
    assertThat(spy.getInvocation().getArguments(), is(args));
    assertThat(result, is(originalValue));
  }

  @Test
  public void testInvokingBoundMappingTwice() throws Throwable {
    final Object originalValue = new Object();
    final SpyingOperation spy = new SpyingOperation(originalValue);
    final SpyingOperation otherSpy = new SpyingOperation(originalValue);
    mappings.add(new ImmutableInvocationMapping<>(Object.class.getMethod("hashCode"), otherSpy));
    mappings.add(
        new ImmutableInvocationMapping<>(
            ReturnTypeSampleRepository.class.getMethod("findOther"), spy));
    assertThat(
        handler.invoke(
            new Object(),
            ReturnTypeSampleRepository.class.getMethod("findOther"),
            new Object[] {1, 2, 3}),
        is(originalValue));
    assertThat(
        handler.invoke(
            new Object(),
            ReturnTypeSampleRepository.class.getMethod("findOther"),
            new Object[] {4, 5, 6}),
        is(originalValue));
    assertThat(otherSpy.getInvocation(), is(nullValue()));
  }
}
