package com.mmnaseri.utils.spring.data.proxy.impl;

import com.mmnaseri.utils.spring.data.repository.DefaultCrudRepository;
import com.mmnaseri.utils.spring.data.repository.DefaultGemfireRepository;
import com.mmnaseri.utils.spring.data.repository.DefaultJpaRepository;
import com.mmnaseri.utils.spring.data.repository.DefaultPagingAndSortingRepository;
import com.mmnaseri.utils.spring.data.error.RepositoryDefinitionException;
import com.mmnaseri.utils.spring.data.proxy.TypeMapping;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.*;
import org.hamcrest.Matchers;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (4/10/16)
 */
public class DefaultTypeMappingContextTest {

    private Class[] defaultImplementations;

    @BeforeMethod
    public void setUp() throws Exception {
        defaultImplementations = new Class[]{DefaultGemfireRepository.class, DefaultJpaRepository.class, DefaultPagingAndSortingRepository.class, DefaultCrudRepository.class};
    }

    @Test
    public void testDefaultMappings() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        assertThat(context.getImplementations(Object.class), Matchers.<Class<?>>containsInAnyOrder(defaultImplementations));
    }

    @Test
    public void testRegisteringMappings() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length));
        context.register(Double.class, Float.class);
        assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length + 1));
        assertThat(context.getImplementations(Double.class), hasItem(Float.class));
    }

    @Test
    public void testRegisteringMappingForSupertype() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length));
        context.register(Number.class, Float.class);
        assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length + 1));
        assertThat(context.getImplementations(Double.class), hasItem(Float.class));
    }

    @Test
    public void testRegisteringOrderedMappings() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length));
        context.register(Number.class, HighPriorityMapping.class);
        context.register(Number.class, LowerPriorityMapping.class);
        assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length + 2));
        assertThat(context.getImplementations(Double.class), hasItem(LowerPriorityMapping.class));
        assertThat(context.getImplementations(Double.class), hasItem(HighPriorityMapping.class));
        assertThat(context.getImplementations(Double.class).indexOf(LowerPriorityMapping.class), is(lessThan(context.getImplementations(Double.class).indexOf(HighPriorityMapping.class))));
    }

    @Test
    public void testRegisteringWithParent() throws Exception {
        final DefaultTypeMappingContext parent = new DefaultTypeMappingContext();
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext(parent);
        assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length));
        parent.register(Number.class, Float.class);
        assertThat(context.getImplementations(Double.class), hasSize(defaultImplementations.length + 1));
        assertThat(context.getImplementations(Double.class), hasItem(Float.class));
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class)
    public void testRegisteringAbstractImplementation() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        context.register(Object.class, AbstractImplementation.class);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class)
    public void testRegisteringInterfaceImplementation() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        context.register(Object.class, InterfaceImplementation.class);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class)
    public void testGettingMappingsWhenConstructorIsPrivate() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        context.register(Number.class, ImplementationWithPrivateConstructor.class);
        context.getMappings(Double.class);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class)
    public void testGettingMappingsWhenClassIsPrivate() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        context.register(Number.class, PrivateImplementationClass.class);
        context.getMappings(Double.class);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class)
    public void testGettingMappingsWhenClassHasNoDefaultConstructor() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        context.register(Number.class, ImplementationWithoutADefaultConstructor.class);
        context.getMappings(Double.class);
    }

    @Test(expectedExceptions = RepositoryDefinitionException.class)
    public void testGettingMappingsWhenClassThrowsErrors() throws Exception {
        final DefaultTypeMappingContext context = new DefaultTypeMappingContext();
        context.register(Number.class, ErrorThrowingImplementation.class);
        context.getMappings(Double.class);
    }

    @Test
    public void testRegisteringAProperImplementation() throws Exception {
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
        assert implementation != null;
        assertThat(implementation.pi(), is(Math.PI));
    }

    private static abstract class AbstractImplementation {}

    private interface InterfaceImplementation {}

    private static class PrivateImplementationClass {}

}