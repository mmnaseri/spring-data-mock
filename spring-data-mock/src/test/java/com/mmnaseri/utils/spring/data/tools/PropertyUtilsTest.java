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
import java.util.Objects;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (9/30/15)
 */
public class PropertyUtilsTest extends AbstractUtilityClassTest {

    @Override
    protected Class<?> getUtilityClass() {
        return PropertyUtils.class;
    }

    @Test(expectedExceptions = ParserException.class,
          expectedExceptionsMessageRegExp = "Expected pattern '.*?' was not encountered.*")
    public void testPropertyPathThatDoesNotStartWithCapitalLetter() {
        PropertyUtils.getPropertyDescriptor(Person.class, "address");
    }

    @Test
    public void testFirstLevelPropertyPath() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "Address");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address"));
        assertThat(descriptor.getType(), equalTo((Class) Address.class));
    }

    @Test
    public void testFirstLevelPropertyWithoutExplicitBreaking() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "AddressZip");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("addressZip"));
        assertThat(descriptor.getType(), equalTo((Class) Zip.class));
    }

    @Test
    public void testPropertyWithExplicitBreaking() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "Address_Zip");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address.zip"));
        assertThat(descriptor.getType(), equalTo((Class) Zip.class));
    }

    @Test
    public void testMultiLevelPropertyWithoutExplicitBreaking() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "AddressZipPrefix");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("addressZip.prefix"));
        assertThat(descriptor.getType(), equalTo((Class) String.class));
    }

    @Test
    public void testMultiLevelPropertyWithExplicitBreaking() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(Person.class, "Address_ZipPrefix");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address.zip.prefix"));
        assertThat(descriptor.getType(), equalTo((Class) String.class));
    }

    @Test(expectedExceptions = IllegalStateException.class,
          expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\.Person`")
    public void testNonExistingFirstLevelProperty() {
        PropertyUtils.getPropertyDescriptor(Person.class, "Xyz");
    }

    @Test(expectedExceptions = IllegalStateException.class,
          expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\.Zip`")
    public void testNonExistingThirdLevelProperty() {
        PropertyUtils.getPropertyDescriptor(Person.class, "Address_ZipXyz");
    }

    @Test
    public void testFirstLevelPropertyWithoutExplicitBreakingUsingFields() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "AddressZip");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("addressZip"));
        assertThat(descriptor.getType(), equalTo((Class) Zip.class));
    }

    @Test
    public void testPropertyWithExplicitBreakingUsingFields() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class,
                                                                                  "Address_Zip");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address.zip"));
        assertThat(descriptor.getType(), equalTo((Class) Zip.class));
    }

    @Test
    public void testMultiLevelPropertyWithoutExplicitBreakingUsingFields() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class,
                                                                                  "AddressZipPrefix");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("addressZip.prefix"));
        assertThat(descriptor.getType(), equalTo((Class) String.class));
    }

    @Test
    public void testMultiLevelPropertyWithExplicitBreakingUsingFields() {
        final PropertyDescriptor descriptor = PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class,
                                                                                  "Address_ZipPrefix");
        assertThat(descriptor, is(notNullValue()));
        assertThat(descriptor.getPath(), is("address.zip.prefix"));
        assertThat(descriptor.getType(), equalTo((Class) String.class));
    }

    @Test(expectedExceptions = IllegalStateException.class,
          expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\.NoAccessorPerson`")
    public void testNonExistingFirstLevelPropertyUsingFields() {
        PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "Xyz");
    }

    @Test(expectedExceptions = IllegalStateException.class,
          expectedExceptionsMessageRegExp = "Could not find property `xyz` on `class .*?\\.Zip`")
    public void testNonExistingThirdLevelPropertyUsingFields() {
        PropertyUtils.getPropertyDescriptor(NoAccessorPerson.class, "Address_ZipXyz");
    }

    @Test
    public void testReadingPropertyValue() {
        final Person person = new Person();
        person.setAddress(new Address());
        person.getAddress().setCity("Tehran");
        person.getAddress().setState(new State());
        person.getAddress().getState().setName("Teheran");
        person.getAddress().getState().setAbbreviation("TEH");
        assertThat(PropertyUtils.getPropertyValue(person, "address"), Matchers.is(person.getAddress()));
        assertThat(PropertyUtils.getPropertyValue(person, "address.city"),
                   Matchers.is(person.getAddress().getCity()));
        assertThat(PropertyUtils.getPropertyValue(person, "address.state.name"),
                   Matchers.is(person.getAddress().getState().getName()));
        assertThat(PropertyUtils.getPropertyValue(person, "address.state.abbreviation"),
                   Matchers.is(person.getAddress().getState().getAbbreviation()));
        assertThat(PropertyUtils.getPropertyValue(person, "firstName"), is(nullValue()));
    }

    @Test
    public void testPrimitiveTypeConversion() {
        assertThat(PropertyUtils.getTypeOf(int.class), is(Matchers.equalTo(Integer.class)));
        assertThat(PropertyUtils.getTypeOf(float.class), is(Matchers.equalTo(Float.class)));
        assertThat(PropertyUtils.getTypeOf(double.class), is(Matchers.equalTo(Double.class)));
        assertThat(PropertyUtils.getTypeOf(byte.class), is(Matchers.equalTo(Byte.class)));
        assertThat(PropertyUtils.getTypeOf(short.class), is(Matchers.equalTo(Short.class)));
        assertThat(PropertyUtils.getTypeOf(long.class), is(Matchers.equalTo(Long.class)));
        assertThat(PropertyUtils.getTypeOf(char.class), is(Matchers.equalTo(Character.class)));
        assertThat(PropertyUtils.getTypeOf(boolean.class), is(Matchers.equalTo(Boolean.class)));
    }

    @Test
    public void testNonPrimitiveTypeConversion() {
        assertThat(PropertyUtils.getTypeOf(Object.class), is(Matchers.equalTo(Object.class)));
        assertThat(PropertyUtils.getTypeOf(String.class), is(Matchers.equalTo(String.class)));
        assertThat(PropertyUtils.getTypeOf(BigDecimal.class), is(Matchers.equalTo(BigDecimal.class)));
        assertThat(PropertyUtils.getTypeOf(Person.class), is(Matchers.equalTo(Person.class)));
    }

    @Test
    public void testPropertyNameFromGetterMethod() {
        assertThat(PropertyUtils.getPropertyName(
                Objects.requireNonNull(ReflectionUtils.findMethod(Person.class, "getAddress"))),
                   is("address"));
        assertThat(PropertyUtils.getPropertyName(
                Objects.requireNonNull(ReflectionUtils.findMethod(Person.class, "getAddressZip"))),
                   is("addressZip"));
    }

    @Test
    public void testReadingPropertyValueThroughField() throws Exception {
        final ClassWithNoGetters object = new ClassWithNoGetters();
        final Field id = Objects.requireNonNull(ReflectionUtils.findField(ClassWithNoGetters.class, "id"));
        id.setAccessible(true);
        id.set(object, "1234");
        assertThat(PropertyUtils.getPropertyValue(object, "id"), is(id.get(object)));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testReadingPropertyValueThroughErrorThrowingGetter() {
        final ClassWithErrorThrowingAccessors object = new ClassWithErrorThrowingAccessors();
        PropertyUtils.getPropertyValue(object, "id");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testReadingNonExistentProperty() {
        final Person person = new Person();
        PropertyUtils.getPropertyValue(person, "address.xyz");
    }

    @Test
    public void testReadingWhenMiddlePropertyIsNull() {
        assertThat(PropertyUtils.getPropertyValue(new Person().setAddress(new Address()), "address.zip.prefix"),
                   is(nullValue()));
    }

    @Test
    public void testSettingImmediatePropertyValueUsingField() throws Exception {
        final ClassWithNoGetters object = new ClassWithNoGetters();
        final Field id = Objects.requireNonNull(ReflectionUtils.findField(ClassWithNoGetters.class, "id"));
        id.setAccessible(true);
        final String value = "12345";
        PropertyUtils.setPropertyValue(object, "id", value);
        assertThat(id.get(object), Matchers.is(value));
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSettingImmediatePropertyValueUsingErrorThrowingSetter() {
        PropertyUtils.setPropertyValue(new ClassWithErrorThrowingAccessors(), "id", "");
    }

    @Test
    public void testSettingImmediateFinalField() {
        PropertyUtils.setPropertyValue(new ClassWithFinalId(), "id", "");
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSettingUnknownProperty() {
        PropertyUtils.setPropertyValue(new Person(), "xyz", "");
    }

    @Test
    public void testSettingPropertyThroughSetters() {
        final Person person = new Person();
        final String value = "123";
        final Object changed = PropertyUtils.setPropertyValue(person, "id", value);
        assertThat(changed, Matchers.is(person));
        assertThat(person.getId(), is(value));
    }

    @Test
    public void testSettingNestedProperty() {
        final Person person = new Person().setAddress(new Address().setZip(new Zip()));
        final String value = "Capital";
        final Object changed = PropertyUtils.setPropertyValue(person, "address.zip.area", value);
        assertThat(changed, Matchers.is(person.getAddress().getZip()));
        assertThat(person.getAddress().getZip().getArea(), is(value));
    }

    @Test
    public void testSettingPropertyToNullThroughASetter() {
        final Person person = new Person().setId("1234");
        PropertyUtils.setPropertyValue(person, "id", null);
        assertThat(person.getId(), is(nullValue()));
    }

    @Test(expectedExceptions = IllegalStateException.class,
          expectedExceptionsMessageRegExp = "Failed to set property value through the field .*")
    public void testSettingPrimitiveValueToNullThroughSetter() {
        PropertyUtils.setPropertyValue(new ClassWithPrimitiveField(), "position", null);
    }

    @Test(expectedExceptions = IllegalStateException.class)
    public void testSettingNestedPropertyWhenParentPropertyIsNull() {
        PropertyUtils.setPropertyValue(new Person(), "address.zip", new Zip());
    }

}