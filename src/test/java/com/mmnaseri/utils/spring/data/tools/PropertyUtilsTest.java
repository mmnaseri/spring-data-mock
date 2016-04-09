package com.mmnaseri.utils.spring.data.tools;

import com.mmnaseri.utils.spring.data.domain.model.Address;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import com.mmnaseri.utils.spring.data.domain.model.State;
import com.mmnaseri.utils.spring.data.domain.model.Zip;
import com.mmnaseri.utils.spring.data.error.ParserException;
import com.mmnaseri.utils.spring.data.query.PropertyDescriptor;
import org.hamcrest.Matchers;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.Test;

import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class PropertyUtilsTest extends AbstractUtilityClassTest {

    private static class NoAccessorPerson {

        private String id;
        private String firstName;
        private String lastName;
        private Zip addressZip;
        private Address address;

    }

    @Override
    protected Class<?> getUtilityClass() {
        return PropertyUtils.class;
    }

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

    @Test
    public void testFirstLevelPropertyWithoutExplicitBreakingUsingFields() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "AddressZip");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("addressZip"));
        assertThat(descriptor.getType(), equalTo((Class) Zip.class));
    }

    @Test
    public void testPropertyWithExplicitBreakingUsingFields() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "Address_Zip");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address.zip"));
        assertThat(descriptor.getType(), equalTo((Class) Zip.class));
    }

    @Test
    public void testMultiLevelPropertyWithoutExplicitBreakingUsingFields() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "AddressZipPrefix");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("addressZip.prefix"));
        assertThat(descriptor.getType(), equalTo((Class) String.class));
    }

    @Test
    public void testMultiLevelPropertyWithExplicitBreakingUsingFields() throws Exception {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "Address_ZipPrefix");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address.zip.prefix"));
        assertThat(descriptor.getType(), equalTo((Class) String.class));
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\$NoAccessorPerson`")
    public void testNonExistingFirstLevelPropertyUsingFields() throws Exception {
        PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "Xyz");
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\.Zip`")
    public void testNonExistingThirdLevelPropertyUsingFields() throws Exception {
        PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "Address_ZipXyz");
    }

    @Test
    public void testReadingPropertyValue() throws Exception {
        final Person person = new Person();
        person.setAddress(new Address());
        person.getAddress().setCity("Tehran");
        person.getAddress().setState(new State());
        person.getAddress().getState().setName("Teheran");
        person.getAddress().getState().setAbbreviation("TEH");
        assertThat(PropertyUtils.getPropertyValue(person, "address"), Matchers.<Object>is(person.getAddress()));
        assertThat(PropertyUtils.getPropertyValue(person, "address.city"), Matchers.<Object>is(person.getAddress().getCity()));
        assertThat(PropertyUtils.getPropertyValue(person, "address.state.name"), Matchers.<Object>is(person.getAddress().getState().getName()));
        assertThat(PropertyUtils.getPropertyValue(person, "address.state.abbreviation"), Matchers.<Object>is(person.getAddress().getState().getAbbreviation()));
        assertThat(PropertyUtils.getPropertyValue(person, "firstName"), Matchers.is(nullValue()));
    }

    @Test
    public void testPrimitiveTypeConversion() throws Exception {
        assertThat(PropertyUtils.getTypeOf(int.class), is(Matchers.<Class<?>>equalTo(Integer.class)));
        assertThat(PropertyUtils.getTypeOf(float.class), is(Matchers.<Class<?>>equalTo(Float.class)));
        assertThat(PropertyUtils.getTypeOf(double.class), is(Matchers.<Class<?>>equalTo(Double.class)));
        assertThat(PropertyUtils.getTypeOf(byte.class), is(Matchers.<Class<?>>equalTo(Byte.class)));
        assertThat(PropertyUtils.getTypeOf(short.class), is(Matchers.<Class<?>>equalTo(Short.class)));
        assertThat(PropertyUtils.getTypeOf(long.class), is(Matchers.<Class<?>>equalTo(Long.class)));
        assertThat(PropertyUtils.getTypeOf(char.class), is(Matchers.<Class<?>>equalTo(Character.class)));
        assertThat(PropertyUtils.getTypeOf(boolean.class), is(Matchers.<Class<?>>equalTo(Boolean.class)));
    }

    @Test
    public void testNonPrimitiveTypeConversion() throws Exception {
        assertThat(PropertyUtils.getTypeOf(Object.class), is(Matchers.<Class<?>>equalTo(Object.class)));
        assertThat(PropertyUtils.getTypeOf(String.class), is(Matchers.<Class<?>>equalTo(String.class)));
        assertThat(PropertyUtils.getTypeOf(BigDecimal.class), is(Matchers.<Class<?>>equalTo(BigDecimal.class)));
        assertThat(PropertyUtils.getTypeOf(Person.class), is(Matchers.<Class<?>>equalTo(Person.class)));
    }

    @Test
    public void testPropertyNameFromGetterMethod() throws Exception {
        assertThat(PropertyUtils.getPropertyName(ReflectionUtils.findMethod(Person.class, "getAddress")), is("address"));
        assertThat(PropertyUtils.getPropertyName(ReflectionUtils.findMethod(Person.class, "getAddressZip")), is("addressZip"));
    }

}