package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.sample.models.Person;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.ReturnTypeSampleRepository;
import com.mmnaseri.utils.spring.data.sample.usecases.proxy.converters.NoOpResultConverter;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class AbstractResultConverterTest {

    @Test
    public void testConversionWhenInputIsNull() throws Exception {
        final Object converted = new NoOpResultConverter().convert(null, null);
        assertThat(converted, is(nullValue()));
    }

    @Test
    public void testConversionWhenTargetReturnsVoid() throws Exception {
        final Object converted = new NoOpResultConverter().convert(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("doSomething"), null), new Object());
        assertThat(converted, is(nullValue()));
    }

    @Test
    public void testWhenTypeMatchesTheReturnType() throws Exception {
        final Person original = new Person();
        final Object converted = new NoOpResultConverter().convert(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findPerson"), null), original);
        assertThat(converted, is(notNullValue()));
        assertThat(converted, is(instanceOf(Person.class)));
        assertThat((Person) converted, is(original));
    }

    @Test
    public void testThatPassesToSubClassOtherwise() throws Exception {
        final Object original = new Object();
        final NoOpResultConverter converter = new NoOpResultConverter();
        final Object converted = converter.convert(
                new ImmutableInvocation(ReturnTypeSampleRepository.class.getMethod("findPerson"), null), original);
        assertThat(converted, is(notNullValue()));
        assertThat(converted, is(original));
        assertThat(converter.getOriginal(), is(original));
    }

}