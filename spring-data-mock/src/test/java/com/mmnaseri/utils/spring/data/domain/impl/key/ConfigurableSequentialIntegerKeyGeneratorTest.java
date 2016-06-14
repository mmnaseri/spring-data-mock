package com.mmnaseri.utils.spring.data.domain.impl.key;

import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (6/14/16, 12:25 PM)
 */
public class ConfigurableSequentialIntegerKeyGeneratorTest {

    @Test
    public void testKeyGenerationWithDefaultSettings() throws Exception {
        final ConfigurableSequentialIntegerKeyGenerator generator = new ConfigurableSequentialIntegerKeyGenerator();
        assertThat(generator.getNext(), is(1));
        assertThat(generator.getNext(), is(2));
        assertThat(generator.getNext(), is(3));
    }

    @Test
    public void testKeyGenerationWithDefaultStepAndCustomInitialValue() throws Exception {
        final int initialValue = 100;
        final ConfigurableSequentialIntegerKeyGenerator generator = new ConfigurableSequentialIntegerKeyGenerator(initialValue);
        assertThat(generator.getNext(), is(initialValue));
        assertThat(generator.getNext(), is(initialValue + 1));
        assertThat(generator.getNext(), is(initialValue + 2));
    }

    @Test
    public void testKeyGenerationWithCustomStepAndCustomInitialValue() throws Exception {
        final int initialValue = 100;
        final int step = 3;
        final ConfigurableSequentialIntegerKeyGenerator generator = new ConfigurableSequentialIntegerKeyGenerator(initialValue, step);
        assertThat(generator.getNext(), is(initialValue));
        assertThat(generator.getNext(), is(initialValue + step));
        assertThat(generator.getNext(), is(initialValue + step * 2));
    }

}