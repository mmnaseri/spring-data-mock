package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import org.testng.annotations.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/14/16, 12:27 PM)
 */
public class ConfigurableSequentialLongKeyGeneratorTest {

  @Test
  public void testKeyGenerationWithDefaultSettings() {
    final KeyGenerator<Long> generator = new ConfigurableSequentialLongKeyGenerator();
    assertThat(generator.generate(), is(1L));
    assertThat(generator.generate(), is(2L));
    assertThat(generator.generate(), is(3L));
  }

  @Test
  public void testKeyGenerationWithDefaultStepAndCustomInitialValue() {
    final long initialValue = 100L;
    final KeyGenerator<Long> generator = new ConfigurableSequentialLongKeyGenerator(initialValue);
    assertThat(generator.generate(), is(initialValue));
    assertThat(generator.generate(), is(initialValue + 1));
    assertThat(generator.generate(), is(initialValue + 2));
  }

  @Test
  public void testKeyGenerationWithCustomStepAndCustomInitialValue() {
    final long initialValue = 100L;
    final long step = 3L;
    final KeyGenerator<Long> generator =
        new ConfigurableSequentialLongKeyGenerator(initialValue, step);
    assertThat(generator.generate(), is(initialValue));
    assertThat(generator.generate(), is(initialValue + step));
    assertThat(generator.generate(), is(initialValue + step * 2));
  }
}
