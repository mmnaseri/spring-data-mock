package com.mmnaseri.utils.spring.data.domain.impl.key;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 12:27 PM)
 */
public class ConfigurableSequentialLongKeyGeneratorTest {

    @Test
    public void testKeyGenerationWithDefaultSettings() throws Exception {
        final ConfigurableSequentialLongKeyGenerator generator = new ConfigurableSequentialLongKeyGenerator();
        assertThat(generator.getNext(), is(1L));
        assertThat(generator.getNext(), is(2L));
        assertThat(generator.getNext(), is(3L));
    }

    @Test
    public void testKeyGenerationWithDefaultStepAndCustomInitialValue() throws Exception {
        final long initialValue = 100L;
        final ConfigurableSequentialLongKeyGenerator generator = new ConfigurableSequentialLongKeyGenerator(initialValue);
        assertThat(generator.getNext(), is(initialValue));
        assertThat(generator.getNext(), is(initialValue + 1));
        assertThat(generator.getNext(), is(initialValue + 2));
    }

    @Test
    public void testKeyGenerationWithCustomStepAndCustomInitialValue() throws Exception {
        final long initialValue = 100L;
        final long step = 3L;
        final ConfigurableSequentialLongKeyGenerator generator = new ConfigurableSequentialLongKeyGenerator(initialValue, step);
        assertThat(generator.getNext(), is(initialValue));
        assertThat(generator.getNext(), is(initialValue + step));
        assertThat(generator.getNext(), is(initialValue + step * 2));
    }

}