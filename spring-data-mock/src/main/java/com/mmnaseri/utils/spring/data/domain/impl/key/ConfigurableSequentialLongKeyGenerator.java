package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.impl.AbstractRandomKeyGenerator;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (6/14/16, 11:47 AM)
 */
public class ConfigurableSequentialLongKeyGenerator extends AbstractRandomKeyGenerator<Long> {

  private final AtomicLong current;
  private final long step;

  public ConfigurableSequentialLongKeyGenerator() {
    this(1L);
  }

  public ConfigurableSequentialLongKeyGenerator(long initialValue) {
    this(initialValue, 1L);
  }

  public ConfigurableSequentialLongKeyGenerator(long initialValue, long step) {
    current = new AtomicLong(initialValue);
    this.step = step;
  }

  @Override
  protected Long getNext() {
    return current.getAndAdd(step);
  }
}
