package com.mmnaseri.utils.spring.data.proxy.impl.converters;

import com.mmnaseri.utils.spring.data.domain.Invocation;
import com.mmnaseri.utils.spring.data.domain.impl.ImmutableInvocation;
import com.mmnaseri.utils.spring.data.domain.model.Person;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Mohammad Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class AbstractResultConverterTest {

    private static class NoOpResultConverter extends AbstractResultConverter {

        private Object original;

        @Override
        protected Object doConvert(Invocation invocation, Object original) {
            this.original = original;
            return original;
        }

        public Object getOriginal() {
            return original;
        }

    }

    private interface Sample {

        void findNothing();

        Person findPerson();

    }

    @Test
    public void testConversionWhenInputIsNull() throws Exception {
        final Object converted = new NoOpResultConverter().convert(null, null);
        assertThat(converted, is(nullValue()));
    }

    @Test
    public void testConversionWhenTargetReturnsVoid() throws Exception {
        final Object converted = new NoOpResultConverter().convert(new ImmutableInvocation(Sample.class.getMethod("findNothing"), null), new Object());
        assertThat(converted, is(nullValue()));
    }

    @Test
    public void testWhenTypeMatchesTheReturnType() throws Exception {
        final Person original = new Person();
        final Object converted = new NoOpResultConverter().convert(new ImmutableInvocation(Sample.class.getMethod("findPerson"), null), original);
        assertThat(converted, is(notNullValue()));
        assertThat(converted, is(instanceOf(Person.class)));
        assertThat((Person) converted, is(original));
    }

    @Test
    public void testThatPassesToSubClassOtherwise() throws Exception {
        final Object original = new Object();
        final NoOpResultConverter converter = new NoOpResultConverter();
        final Object converted = converter.convert(new ImmutableInvocation(Sample.class.getMethod("findPerson"), null), original);
        assertThat(converted, is(notNullValue()));
        assertThat(converted, is(original));
        assertThat(converter.getOriginal(), is(original));
    }

}