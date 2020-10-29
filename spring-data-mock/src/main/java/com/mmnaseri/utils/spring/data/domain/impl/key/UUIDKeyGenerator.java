package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.util.UUID;

/**
 * This class will generate unique UUIDs for use in entities whose keys are loose String values.
 *
 * @author Milad Naseri (m.m.naseri@gmail.com)
 * @since 1.0 (10/6/15)
 */
public class UUIDKeyGenerator implements KeyGenerator<String> {

  @Override
  public String generate() {
    return UUID.randomUUID().toString();
  }
}
