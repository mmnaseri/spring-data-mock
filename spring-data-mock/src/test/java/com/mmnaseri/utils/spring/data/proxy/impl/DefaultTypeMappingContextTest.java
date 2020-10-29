package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.repository.DefaultCrudRepository;
import com.mmnaseri.utils.spring.data.repository.DefaultGemfireRepository;
import com.mmnaseri.utils.spring.data.repository.DefaultJpaRepository;
import com.mmnaseri.utils.spring.data.repository.DefaultPagingAndSortingRepository;
import com.mmnaseri.utils.spring.data.repository.DefaultQueryByExampleExecutor;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ErrorThrowingImplementation;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.HighPriorityMapping;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ImplementationWithPrivateConstructor;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ImplementationWithoutADefaultConstructor;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.LowerPriorityMapping;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ProperImplementation;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.instanceOf;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.lessThan;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultTypeMappingContextTest {

  private Class[] defaultImplementations;

  @BeforeMethod
  public void setUp() {
    defaultImplementations =
        new Class[] {
          DefaultGemfireRepository.class,
          DefaultJpaRepository.class,
          DefaultPagingAndSortingRepository.class,
          DefaultCrudRepository.class,
          DefaultQueryByExampleExecutor.class
        };
  }

  @Test
  public void testDefaultMappings() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    assertThat(
        context.getImplementations(Object.class),
        Matchers.<Class<?>>containsInAnyOrder(defaultImplementations));
  }

  @Test
  public void testRegisteringMappings() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length));
    context.register(Double.class, Float.class);
    assertThat(
        context.getImplementations(Double.class), hasSize(defaultImplementations.length + 1));
    assertThat(context.getImplementations(Double.class), hasItem(Float.class));
  }

  @Test
  public void testRegisteringMappingForSupertype() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length));
    context.register(Number.class, Float.class);
    assertThat(
        context.getImplementations(Double.class), hasSize(defaultImplementations.length + 1));
    assertThat(context.getImplementations(Double.class), hasItem(Float.class));
  }

  @Test
  public void testRegisteringOrderedMappings() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length));
    context.register(Number.class, HighPriorityMapping.class);
    context.register(Number.class, LowerPriorityMapping.class);
    assertThat(
        context.getImplementations(Double.class), hasSize(defaultImplementations.length + 2));
    assertThat(context.getImplementations(Double.class), hasItem(LowerPriorityMapping.class));
    assertThat(context.getImplementations(Double.class), hasItem(HighPriorityMapping.class));
    assertThat(
        context.getImplementations(Double.class).indexOf(LowerPriorityMapping.class),
        is(lessThan(context.getImplementations(Double.class).indexOf(HighPriorityMapping.class))));
  }

  @Test
  public void testRegisteringWithParent() {
    final DefaultTypeMappingContext parent = new DefaultTypeMappingContext();
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext(parent);
    assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length));
    parent.register(Number.class, Float.class);
    assertThat(
        context.getImplementations(Double.class), hasSize(defaultImplementations.length + 1));
    assertThat(context.getImplementations(Double.class), hasItem(Float.class));
  }

  @Test(expectedExceptions = RepositoryDefinitionException.class)
  public void testRegisteringAbstractImplementation() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    context.register(Object.class, AbstractImplementation.class);
  }

  @Test(expectedExceptions = RepositoryDefinitionException.class)
  public void testRegisteringInterfaceImplementation() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    context.register(Object.class, InterfaceImplementation.class);
  }

  @Test(expectedExceptions = RepositoryDefinitionException.class)
  public void testGettingMappingsWhenConstructorIsPrivate() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    context.register(Number.class, ImplementationWithPrivateConstructor.class);
    context.getMappings(Double.class);
  }

  @Test(expectedExceptions = RepositoryDefinitionException.class)
  public void testGettingMappingsWhenClassIsPrivate() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    context.register(Number.class, PrivateImplementationClass.class);
    context.getMappings(Double.class);
  }

  @Test(expectedExceptions = RepositoryDefinitionException.class)
  public void testGettingMappingsWhenClassHasNoDefaultConstructor() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    context.register(Number.class, ImplementationWithoutADefaultConstructor.class);
    context.getMappings(Double.class);
  }

  @Test(expectedExceptions = RepositoryDefinitionException.class)
  public void testGettingMappingsWhenClassThrowsErrors() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    context.register(Number.class, ErrorThrowingImplementation.class);
    context.getMappings(Double.class);
  }

  @Test
  public void testRegisteringAProperImplementation() {
    final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
    context.register(Number.class, ProperImplementation.class);
    final List<TypeMapping<?>> mappings = context.getMappings(Double.class);
    assertThat(mappings, hasSize(defaultImplementations.length + 1));
    ProperImplementation implementation = null;
    for (TypeMapping<?> mapping : mappings) {
      assertThat(mapping.getInstance(), is(instanceOf(mapping.getType())));
      if (mapping.getType().equals(ProperImplementation.class)) {
        implementation = (ProperImplementation) mapping.getInstance();
      }
    }
    assertThat(implementation, is(notNullValue()));
    assertThat(implementation.pi(), is(Math.PI));
  }

  private interface InterfaceImplementation {}

  private abstract static class AbstractImplementation {}

  private static class PrivateImplementationClass {}
}
