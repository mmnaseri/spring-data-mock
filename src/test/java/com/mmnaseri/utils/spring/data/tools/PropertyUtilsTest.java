package com.mmnaseri.utils.spring.data.tools;

import com.mmnaseri.utils.spring.data.domain.model.Address;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.domain.model.Zip;
import com.mmnaseri.utils.spring.data.error.ParserException;
import com.mmnaseri.utils.spring.data.query.PropertyDescriptor;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class PropertyUtilsTest {

    @Test(expectedExceptions = ParserException.class, expectedExceptionsMessageRegExp = "Expected pattern '.*?' was not encountered.*")
    public void testPropertyPathThatDoesNotStartWithCapitalLetter() throws Exception {
        PropertyUtils.getPropertyDescriptor(Person.class, "address");
    }

    @Test
    public void testFirstLevelPropertyPath() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "Address");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address"));
        assertThat(descriptor.getType(), equalTo((Class) Address.class));
    }

    @Test
    public void testFirstLevelPropertyWithoutExplicitBreaking() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "AddressZip");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("addressZip"));
        assertThat(descriptor.getType(), equalTo((Class) Zip.class));
    }

    @Test
    public void testPropertyWithExplicitBreaking() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "Address_Zip");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address.zip"));
        assertThat(descriptor.getType(), equalTo((Class) Zip.class));
    }

    @Test
    public void testMultiLevelPropertyWithoutExplicitBreaking() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "AddressZipPrefix");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("addressZip.prefix"));
        assertThat(descriptor.getType(), equalTo((Class) String.class));
    }

    @Test
    public void testMultiLevelPropertyWithExplicitBreaking() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "Address_ZipPrefix");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address.zip.prefix"));
        assertThat(descriptor.getType(), equalTo((Class) String.class));
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\.Person`")
    public void testNonExistingFirstLevelProperty() throws Exception {
        PropertyUtils.getPropertyDescriptor(Person.class, "Xyz");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\.Zip`")
    public void testNonExistingThirdLevelProperty() throws Exception {
        PropertyUtils.getPropertyDescriptor(Person.class, "Address_ZipXyz");
    }

}