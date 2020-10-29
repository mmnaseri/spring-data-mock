package com.mmnaseri.utils.spring.data.domain.impl.key;

import org.bson.types.ObjectId;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.contains;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;

public class BsonObjectIdKeyGeneratorTest {

  private BsonObjectIdKeyGenerator generator;

  @BeforeMethod
  public void setUp() {
    generator = new BsonObjectIdKeyGenerator();
  }

  @Test
  public void testNotNullValue() {
    assertThat(generator.generate(), is(notNullValue()));
  }

  @Test
  public void testUniqueness() {
    final Set<ObjectId> set = new HashSet<>();
    for (int i = 0; i < 1000; i++) {
      final ObjectId id = generator.generate();
      assertThat(set, not(contains(id)));
    }
  }
}
