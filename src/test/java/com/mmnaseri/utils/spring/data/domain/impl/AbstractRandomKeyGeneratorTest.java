package com.mmnaseri.utils.spring.data.domain.impl;

import com.mmnaseri.utils.spring.data.domain.KeyGenerator;
import org.testng.annotations.Test;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.not;

/**
 * @author Milad Naseri (milad.naseri@cdk.com)
 * @since 1.0 (4/12/16, 3:28 PM)
 */
public class AbstractRandomKeyGeneratorTest {

    @Test
    public void testThatGeneratorProtectsUsFromDuplicates() throws Exception {
        final Set<Integer> generatedKeys = new HashSet<>();
        final KeyGenerator<Integer> generator = new DuplicatingKeyGenerator();
        for (int i = 0; i < 1000; i++) {
            final Integer key = generator.generate();
            assertThat(generatedKeys, not(hasItem(key)));
            generatedKeys.add(key);
        }
    }

    private static class DuplicatingKeyGenerator extends AbstractRandomKeyGenerator<Integer> {

        private boolean repeat = false;
        private AtomicInteger counter = new AtomicInteger(0);

        @Override
        protected Integer getNext() {
            if (!repeat) {
                counter.incrementAndGet();
                repeat = true;
            } else {
                repeat = false;
            }
            return counter.get();
        }
    }

}