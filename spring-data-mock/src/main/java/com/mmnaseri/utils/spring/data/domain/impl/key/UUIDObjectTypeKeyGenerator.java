package com.mmnaseri.utils.spring.data.domain.impl.key;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;

import java.util.UUID;

/**
 * This class will generate unique UUIDs for use in entities.
 */
public class UUIDObjectTypeKeyGenerator implements KeyGenerator<UUID> {

  @Override
  public UUID generate() {
    return UUID.randomUUID();
  }
}
