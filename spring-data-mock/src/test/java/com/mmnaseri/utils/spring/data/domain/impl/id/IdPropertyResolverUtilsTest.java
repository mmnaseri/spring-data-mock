package com.mmnaseri.utils.spring.data.domain.impl.id;

import com.mmnaseri.utils.spring.data.error.PropertyTypeMismatchException;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithAnnotatedIdField;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithAnnotatedIdFieldFromJPA;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithAnnotatedIdGetter;
import com.mmnaseri.utils.spring.data.sample.models.EntityWithAnnotatedIdGetterFromJPA;
import com.mmnaseri.utils.spring.data.tools.AbstractUtilityClassTest;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.util.List;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.notNullValue;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/8/16, 1:49 AM)
 */
public class IdPropertyResolverUtilsTest extends AbstractUtilityClassTest {

    @Override
    protected Class<?> getUtilityClass() {
        return IdPropertyResolverUtils.class;
    }

    @Test
    public void testReadingAnnotationFromField() throws Exception {
        assertThat(IdPropertyResolverUtils
                           .isAnnotated(EntityWithAnnotatedIdFieldFromJPA.class.getDeclaredField("customIdProperty")),
                   is(true));
        assertThat(IdPropertyResolverUtils.isAnnotated(EntityWithAnnotatedIdField.class.getDeclaredField("id")),
                   is(true));
    }

    @Test
    public void testReadingAnnotationFromMethod() throws Exception {
        assertThat(IdPropertyResolverUtils
                           .isAnnotated(EntityWithAnnotatedIdGetterFromJPA.class.getDeclaredMethod("getMyCustomId")),
                   is(true));
        assertThat(IdPropertyResolverUtils.isAnnotated(EntityWithAnnotatedIdGetter.class.getDeclaredMethod("getId")),
                   is(true));
    }

    @Test(expectedExceptions = PropertyTypeMismatchException.class)
    public void testPropertyNameFromMethodWhenIdTypeIsInvalid() throws Exception {
        IdPropertyResolverUtils.getPropertyNameFromAnnotatedMethod(EntityWithAnnotatedIdGetterFromJPA.class, Long.class,
                                                                   EntityWithAnnotatedIdGetterFromJPA.class
                                                                           .getDeclaredMethod("getMyCustomId"));
    }

    @Test
    public void testPropertyNameFromMethod() throws Exception {
        final String propertyName = IdPropertyResolverUtils.getPropertyNameFromAnnotatedMethod(
                EntityWithAnnotatedIdGetterFromJPA.class, Integer.class,
                EntityWithAnnotatedIdGetterFromJPA.class.getDeclaredMethod("getMyCustomId"));
        assertThat(propertyName, is(notNullValue()));
        assertThat(propertyName, is("myCustomId"));
    }

    @Test
    public void testDeclaringAnnotationsThatAreNotPresent() throws Exception {
        final Field idAnnotations = IdPropertyResolverUtils.class.getDeclaredField("ID_ANNOTATIONS");
        idAnnotations.setAccessible(true);
        final List list = (List) idAnnotations.get(null);
        //noinspection unchecked
        list.add("random class name");
        final String propertyName = IdPropertyResolverUtils.getPropertyNameFromAnnotatedMethod(
                EntityWithAnnotatedIdGetterFromJPA.class, Integer.class,
                EntityWithAnnotatedIdGetterFromJPA.class.getDeclaredMethod("getMyCustomId"));
        assertThat(propertyName, is(notNullValue()));
    }

}