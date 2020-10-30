package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import com.mmnaseri.utils.spring.data.domain.impl.key.RandomIntegerKeyGenerator;

/**
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/8/15)
 */
public class RandomIntegerKeyGeneratorTest extends BaseKeyGeneratorTest<Integer> {

  @Override
  protected KeyGenerator<Integer> getKeyGenerator() {
    return new RandomIntegerKeyGenerator();
  }
}
