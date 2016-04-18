package com.mmnaseri.utils.spring.data.tools;

import com.mmnaseri.utils.spring.data.error.ParserException;
import com.mmnaseri.utils.spring.data.query.PropertyDescriptor;
import com.mmnaseri.utils.spring.data.sample.models.*;
import com.mmnaseri.utils.spring.data.sample.usecases.tools.ClassWithErrorThrowingAccessors;
import com.mmnaseri.utils.spring.data.sample.usecases.tools.ClassWithFinalId;
import com.mmnaseri.utils.spring.data.sample.usecases.tools.ClassWithNoGetters;
import com.mmnaseri.utils.spring.data.sample.usecases.tools.ClassWithPrimitiveField;
import org.hamcrest.Matchers;
import org.springframework.util.ReflectionUtils;
import org.testng.annotations.Test;

import java.lang.reflect.Field;
import java.math.BigDecimal;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (mmnaseri@programmer.net)
 * @since 1.0 (9/30/15)
 */
public class PropertyUtilsTest extends AbstractUtilityClassTest {

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

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\.NoAccessorPerson`")
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
        assertThat(PropertyUtils.getPropertyValue(person, "firstName"), is(nullValue()));
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

    @Test
    public void testReadingPropertyValueThroughField() throws Exception {
        final ClassWithNoGetters object = new ClassWithNoGetters();
        final Field id = ReflectionUtils.findField(ClassWithNoGetters.class, "id");
        id.setAccessible(true);
        id.set(object, "1234");
        assertThat(PropertyUtils.getPropertyValue(object, "id"), is(id.get(object)));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testReadingPropertyValueThroughErrorThrowingGetter() throws Exception {
        final ClassWithErrorThrowingAccessors object = new ClassWithErrorThrowingAccessors();
        PropertyUtils.getPropertyValue(object, "id");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testReadingNonExistentProperty() throws Exception {
        final Person person = new Person();
        PropertyUtils.getPropertyValue(person, "address.xyz");
    }

    @Test
    public void testReadingWhenMiddlePropertyIsNull() throws Exception {
        assertThat(PropertyUtils.getPropertyValue(new Person().setAddress(new Address()), "address.zip.prefix"), is(nullValue()));
    }

    @Test
    public void testSettingImmediatePropertyValueUsingField() throws Exception {
        final ClassWithNoGetters object = new ClassWithNoGetters();
        final Field id = ReflectionUtils.findField(ClassWithNoGetters.class, "id");
        id.setAccessible(true);
        final String value = "12345";
        PropertyUtils.setPropertyValue(object, "id", value);
        assertThat(id.get(object), Matchers.<Object>is(value));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSettingImmediatePropertyValueUsingErrorThrowingSetter() throws Exception {
        PropertyUtils.setPropertyValue(new ClassWithErrorThrowingAccessors(), "id", "");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSettingImmediateFinalField() throws Exception {
        PropertyUtils.setPropertyValue(new ClassWithFinalId(), "id", "");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSettingUnknownProperty() throws Exception {
        PropertyUtils.setPropertyValue(new Person(), "xyz", "");
    }

    @Test
    public void testSettingPropertyThroughSetters() throws Exception {
        final Person person = new Person();
        final String value = "123";
        final Object changed = PropertyUtils.setPropertyValue(person, "id", value);
        assertThat(changed, Matchers.<Object>is(person));
        assertThat(person.getId(), is(value));
    }

    @Test
    public void testSettingNestedProperty() throws Exception {
        final Person person = new Person().setAddress(new Address().setZip(new Zip()));
        final String value = "Capital";
        final Object changed = PropertyUtils.setPropertyValue(person, "address.zip.area", value);
        assertThat(changed, Matchers.<Object>is(person.getAddress().getZip()));
        assertThat(person.getAddress().getZip().getArea(), is(value));
    }

    @Test
    public void testSettingPropertyToNullThroughASetter() throws Exception {
        final Person person = new Person().setId("1234");
        PropertyUtils.setPropertyValue(person, "id", null);
        assertThat(person.getId(), is(nullValue()));
    }

    @Test(expectedExceptions = IllegalStateException.class, expectedExceptionsMessageRegExp = "Failed to set property value through the field .*")
    public void testSettingPrimitiveValueToNullThroughSetter() throws Exception {
        PropertyUtils.setPropertyValue(new ClassWithPrimitiveField(), "position", null);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSettingNestedPropertyWhenParentPropertyIsNull() throws Exception {
        PropertyUtils.setPropertyValue(new Person(), "address.zip", new Zip());
    }

}