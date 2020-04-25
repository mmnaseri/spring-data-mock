package com.mmnaseri.utils.spring.data.tools;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/5/15)
 */
public class StringUtilsTest extends AbstractUtilityClassTest {

    @Override
    protected Class<?> getUtilityClass() {
        return StringUtils.class;
    }

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "String value cannot be null")
    public void testCapitalizingNullValue() throws Exception {
        StringUtils.capitalize(null);
    }

    @Test
    public void testCapitalizingEmptyString() throws Exception {
        assertThat(StringUtils.capitalize(""), is(""));
    }

    @Test
    public void testCapitalizingUncapitalizedString() throws Exception {
        assertThat(StringUtils.capitalize("hello"), is("Hello"));
    }

    @Test
    public void testCapitalizingCapitalizedString() throws Exception {
        assertThat(StringUtils.capitalize("Hello"), is("Hello"));
    }

    @Test(expectedExceptions = NullPointerException.class,
          expectedExceptionsMessageRegExp = "String value cannot be null")
    public void testUncapitalizingNullValue() throws Exception {
        StringUtils.uncapitalize(null);
    }

    @Test
    public void testUncapitalizingEmptyString() throws Exception {
        assertThat(StringUtils.uncapitalize(""), is(""));
    }

    @Test
    public void testUncapitalizingUncapitalizedString() throws Exception {
        assertThat(StringUtils.uncapitalize("hello"), is("hello"));
    }

    @Test
    public void testUncapitalizingCapitalizedString() throws Exception {
        assertThat(StringUtils.uncapitalize("Hello"), is("hello"));
    }
}