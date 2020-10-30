package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/14/16, 12:25 PM)
 */
public class ConfigurableSequentialIntegerKeyGeneratorTest {

  @Test
  public void testKeyGenerationWithDefaultSettings() {
    final KeyGenerator<Integer> generator = new ConfigurableSequentialIntegerKeyGenerator();
    assertThat(generator.generate(), is(1));
    assertThat(generator.generate(), is(2));
    assertThat(generator.generate(), is(3));
  }

  @Test
  public void testKeyGenerationWithDefaultStepAndCustomInitialValue() {
    final int initialValue = 100;
    final KeyGenerator<Integer> generator =
        new ConfigurableSequentialIntegerKeyGenerator(initialValue);
    assertThat(generator.generate(), is(initialValue));
    assertThat(generator.generate(), is(initialValue + 1));
    assertThat(generator.generate(), is(initialValue + 2));
  }

  @Test
  public void testKeyGenerationWithCustomStepAndCustomInitialValue() {
    final int initialValue = 100;
    final int step = 3;
    final KeyGenerator<Integer> generator =
        new ConfigurableSequentialIntegerKeyGenerator(initialValue, step);
    assertThat(generator.generate(), is(initialValue));
    assertThat(generator.generate(), is(initialValue + step));
    assertThat(generator.generate(), is(initialValue + step * 2));
  }
}
